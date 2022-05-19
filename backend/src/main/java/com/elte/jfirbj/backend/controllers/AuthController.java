package com.elte.jfirbj.backend.controllers;

import javax.validation.Valid;

import com.elte.jfirbj.backend.models.enums.RoleEnum;
import com.elte.jfirbj.backend.models.User;
import com.elte.jfirbj.backend.payload.response.JwtResponse;
import com.elte.jfirbj.backend.payload.response.MessageResponse;
import com.elte.jfirbj.backend.repository.UserRepository;
import com.elte.jfirbj.backend.security.jwt.JwtUtils;
import com.elte.jfirbj.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elte.jfirbj.backend.payload.request.LoginRequest;
import com.elte.jfirbj.backend.payload.request.SignupRequest;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AuthController extends com.elte.jfirbj.backend.controllers.utils.AuthUtil {

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = getAuthenticate(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = getAllRoles(userDetails);

        updateCurrentUser(userDetails);

        return ResponseEntity.ok(new JwtResponse(getJwt(authentication), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
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