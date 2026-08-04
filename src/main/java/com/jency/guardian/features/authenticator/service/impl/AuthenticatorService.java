package com.jency.guardian.features.authenticator.service.impl;

import com.jency.guardian.common.dto.response.ApiResponse;
import com.jency.guardian.features.authenticator.dto.request.DisableMfaRequest;
import com.jency.guardian.features.authenticator.dto.request.RegisterAuthenticatorRequest;
import com.jency.guardian.features.authenticator.dto.request.VerifyOtpRequest;
import com.jency.guardian.features.authenticator.dto.response.DeviceResponse;
import com.jency.guardian.features.authenticator.dto.response.RegisterAuthenticatorResponse;
import com.jency.guardian.features.authenticator.dto.response.VerifyOtpResponse;

public interface AuthenticatorService {

    RegisterAuthenticatorResponse register(RegisterAuthenticatorRequest request);
    VerifyOtpResponse verifyOtp(VerifyOtpRequest request);
    DeviceResponse getRegisteredDevice();
    ApiResponse deleteDevice();
    ApiResponse disableMfa(DisableMfaRequest request);
}
