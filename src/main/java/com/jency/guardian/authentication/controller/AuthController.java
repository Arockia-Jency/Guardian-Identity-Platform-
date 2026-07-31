package com.jency.guardian.authentication.controller;


import com.jency.guardian.authentication.dto.request.LoginRequest;
import com.jency.guardian.authentication.dto.request.RegisterRequest;
import com.jency.guardian.authentication.dto.response.LoginResponse;
import com.jency.guardian.authentication.dto.response.RegisterResponse;
import com.jency.guardian.authentication.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}