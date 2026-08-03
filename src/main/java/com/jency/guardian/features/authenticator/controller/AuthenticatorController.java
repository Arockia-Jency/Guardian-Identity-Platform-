package com.jency.guardian.features.authenticator.controller;

import com.jency.guardian.features.authenticator.dto.request.RegisterAuthenticatorRequest;
import com.jency.guardian.features.authenticator.dto.response.RegisterAuthenticatorResponse;
import com.jency.guardian.features.authenticator.service.impl.AuthenticatorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authenticator")
public class AuthenticatorController {

    private final AuthenticatorService authenticatorService;

    public AuthenticatorController(AuthenticatorService authenticatorService) {
        this.authenticatorService = authenticatorService;
    }

    @PostMapping("/register")
    public RegisterAuthenticatorResponse register(
            @RequestBody RegisterAuthenticatorRequest request) {

        return authenticatorService.register(request);
    }
}