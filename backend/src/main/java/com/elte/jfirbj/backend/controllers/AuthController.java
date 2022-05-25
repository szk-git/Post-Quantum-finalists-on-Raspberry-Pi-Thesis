package com.elte.jfirbj.backend.controllers;

import javax.validation.Valid;

import com.elte.jfirbj.backend.controllers.utils.AuthUtil;
import com.elte.jfirbj.backend.models.User;
import com.elte.jfirbj.backend.payload.response.JwtResponse;
import com.elte.jfirbj.backend.payload.response.MessageResponse;
import com.elte.jfirbj.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elte.jfirbj.backend.payload.request.LoginRequest;
import com.elte.jfirbj.backend.payload.request.SignupRequest;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends AuthUtil {

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        ResponseEntity<MessageResponse> body = throwErrorIfUserNotExists(loginRequest);
        if (body != null) return body;

        Authentication authentication = getAuthenticate(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = getAllRoles(userDetails);

        updateCurrentUser(userDetails);

        return ResponseEntity.ok(new JwtResponse(getJwt(authentication), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        ResponseEntity<MessageResponse> body = throwBadRequestIfUsernameOrEmailIsExist(signUpRequest);
        if (body != null) return body;

        User user = new User.UserBuilder()
                .userName(signUpRequest.getUsername())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        saveCurrentUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}