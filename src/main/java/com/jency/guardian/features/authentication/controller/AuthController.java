package com.jency.guardian.features.authentication.controller;


import com.jency.guardian.features.authentication.dto.request.LoginOtpVerifyRequest;
import com.jency.guardian.features.authentication.dto.request.LoginRequest;
import com.jency.guardian.features.authentication.dto.request.RegisterRequest;
import com.jency.guardian.features.authentication.dto.response.LoginOtpVerifyResponse;
import com.jency.guardian.features.authentication.dto.response.LoginResponse;
import com.jency.guardian.features.authentication.dto.response.RegisterResponse;
import com.jency.guardian.features.authentication.service.AuthService;
import com.jency.guardian.features.authenticator.dto.request.VerifyRecoveryCodeRequest;
import com.jency.guardian.features.authenticator.dto.response.RecoveryCodesResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/login/verify-otp")
    public LoginOtpVerifyResponse verifyLoginOtp(@RequestBody LoginOtpVerifyRequest request){
        return authService.verifyLoginOtp(request);
    }
    @PostMapping("/recovery-codes")
    public RecoveryCodesResponse generateRecoveryCodes() {
        return authService.generateRecoveryCodes();
    }

    @PostMapping("/login/recovery")
    public LoginResponse verifyRecoveryCode(@RequestBody VerifyRecoveryCodeRequest request) {
        return authService.verifyRecoveryCode(request);
    }

}