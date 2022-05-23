package com.elte.jfirbj.backend.controllers.utils;

import com.elte.jfirbj.backend.models.User;
import com.elte.jfirbj.backend.models.enums.RoleEnum;
import com.elte.jfirbj.backend.payload.request.LoginRequest;
import com.elte.jfirbj.backend.payload.request.SignupRequest;
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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthUtil {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;

    protected void updateCurrentUser(UserDetailsImpl userDetails) {
        User userById = userRepository.getOne(userDetails.getId());
        userById.setLastLogin(new Date());
        userRepository.save(userById);
    }

    protected List<String> getAllRoles(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    protected Authentication getAuthenticate(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    protected String getJwt(Authentication authentication) {
        return jwtUtils.generateJwtToken(authentication);
    }

    protected void saveCurrentUser(User user) {
        Set<RoleEnum> roles = new HashSet<>();
        roles.add(RoleEnum.ROLE_USER);

        user.setRoles(roles);
        userRepository.save(user);
    }

    protected ResponseEntity<MessageResponse> throwBadRequestIfUsernameOrEmailIsExist(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        return null;
    }

    protected ResponseEntity<MessageResponse> throwErrorIfUserNotExists(LoginRequest signUpRequest) {
        if (!userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Incorrect username!"));
        }
        return null;
    }
}
