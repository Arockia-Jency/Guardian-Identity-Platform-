package com.jency.guardian.features.authentication.service;

import com.jency.guardian.features.authentication.dto.request.LoginOtpVerifyRequest;
import com.jency.guardian.features.authentication.dto.request.LoginRequest;
import com.jency.guardian.features.authentication.dto.request.RegisterRequest;
import com.jency.guardian.features.authentication.dto.response.LoginOtpVerifyResponse;
import com.jency.guardian.features.authentication.dto.response.LoginResponse;
import com.jency.guardian.features.authentication.dto.response.RegisterResponse;
import com.jency.guardian.features.authenticator.dto.request.VerifyRecoveryCodeRequest;
import com.jency.guardian.features.authenticator.dto.response.RecoveryCodesResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginOtpVerifyResponse verifyLoginOtp(LoginOtpVerifyRequest request);

    RecoveryCodesResponse generateRecoveryCodes();

    LoginResponse verifyRecoveryCode(VerifyRecoveryCodeRequest request);
}
