package com.jency.guardian.features.authenticator.controller;

import com.jency.guardian.common.dto.response.ApiResponse;
import com.jency.guardian.features.authenticator.dto.request.DisableMfaRequest;
import com.jency.guardian.features.authenticator.dto.request.RegisterAuthenticatorRequest;
import com.jency.guardian.features.authenticator.dto.request.VerifyOtpRequest;
import com.jency.guardian.features.authenticator.dto.response.DeviceResponse;
import com.jency.guardian.features.authenticator.dto.response.RegisterAuthenticatorResponse;
import com.jency.guardian.features.authenticator.dto.response.VerifyOtpResponse;
import com.jency.guardian.features.authenticator.service.impl.AuthenticatorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authenticator")
public class AuthenticatorController {

    private final AuthenticatorService authenticatorService;

    public AuthenticatorController(AuthenticatorService authenticatorService) {
        this.authenticatorService = authenticatorService;
    }

    @PostMapping("/register")
    public RegisterAuthenticatorResponse register( @Valid @RequestBody RegisterAuthenticatorRequest request) {
        return authenticatorService.register(request);
    }

    @PostMapping("/verify")
    public VerifyOtpResponse verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return authenticatorService.verifyOtp(request);
    }
    @GetMapping("/device")
    public DeviceResponse getRegisteredDevice() {
        return authenticatorService.getRegisteredDevice();
    }

    @DeleteMapping("/device")
    public ApiResponse deleteDevice() {
        return authenticatorService.deleteDevice();
    }

    @PostMapping("/disable")
    public ApiResponse disableMfa(@Valid @RequestBody DisableMfaRequest request) {
        return authenticatorService.disableMfa(request);
    }
}