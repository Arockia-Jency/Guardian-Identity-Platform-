package com.jency.guardian.features.authenticator.service.impl;

import com.jency.guardian.features.authenticator.dto.request.RegisterAuthenticatorRequest;
import com.jency.guardian.features.authenticator.dto.response.RegisterAuthenticatorResponse;

public interface AuthenticatorService {

    RegisterAuthenticatorResponse register(RegisterAuthenticatorRequest request);

}
