package com.jency.guardian.features.authentication.service;

import com.jency.guardian.features.authentication.dto.request.*;
import com.jency.guardian.features.authentication.dto.response.*;
import com.jency.guardian.features.authenticator.dto.request.VerifyRecoveryCodeRequest;
import com.jency.guardian.features.authenticator.dto.response.RecoveryCodesResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginOtpVerifyResponse verifyLoginOtp(LoginOtpVerifyRequest request);

    RecoveryCodesResponse generateRecoveryCodes();

    LoginResponse verifyRecoveryCode(VerifyRecoveryCodeRequest request);

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);

    ResetPasswordResponse resetPassword(ResetPasswordRequest request);

    VerifyResetOtpResponse verifyResetOtp(VerifyResetOtpRequest request);

    VerifyEmailOtpResponse verifyEmailOtp(VerifyEmailOtpRequest request);
}
