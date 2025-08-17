package com.example.user_service.controller;

import com.example.user_service.DTO.LoginRequest;
import com.example.user_service.DTO.RegisterRequest;
import com.example.user_service.model.User;
import com.example.user_service.security.JwtTokenProvider;
import com.example.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;

    private final UserService userService;

    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Extract role from user details
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        // Generate JWT token (already contains username & roles)
        String token = jwtTokenProvider.generateToken(userDetails.getUsername(), role);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest) {
        User savedUser = userService.registerUser(registerRequest);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully " + savedUser.getUsername());
        return ResponseEntity.ok(response);
    }

}
