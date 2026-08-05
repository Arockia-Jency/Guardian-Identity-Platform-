package com.jency.guardian.features.authentication.controller;


import com.jency.guardian.features.authentication.dto.request.*;
import com.jency.guardian.features.authentication.dto.response.*;
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
    public LoginOtpVerifyResponse verifyLoginOtp( @Valid @RequestBody LoginOtpVerifyRequest request){
        return authService.verifyLoginOtp(request);
    }
    @PostMapping("/recovery-codes")
    public RecoveryCodesResponse generateRecoveryCodes() {
        return authService.generateRecoveryCodes();
    }

    @PostMapping("/login/recovery")
    public LoginResponse verifyRecoveryCode( @Valid @RequestBody VerifyRecoveryCodeRequest request) {
        return authService.verifyRecoveryCode(request);
    }

    @PostMapping("/forgot-password")
    public ForgotPasswordResponse forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public ResetPasswordResponse resetPassword(
            @RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }

    @PostMapping("/verify-reset-otp")
    public VerifyResetOtpResponse verifyResetOtp(@RequestBody VerifyResetOtpRequest request) {
        return authService.verifyResetOtp(request);
    }

    @PostMapping("/verify-email-otp")
    public VerifyEmailOtpResponse verifyEmailOtp(
            @Valid @RequestBody VerifyEmailOtpRequest request) {
        return authService.verifyEmailOtp(request);
    }

}