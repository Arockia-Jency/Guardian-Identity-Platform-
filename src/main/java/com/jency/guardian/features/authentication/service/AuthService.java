package com.jency.guardian.features.authentication.service;

import com.jency.guardian.features.authentication.dto.request.LoginRequest;
import com.jency.guardian.features.authentication.dto.request.RegisterRequest;
import com.jency.guardian.features.authentication.dto.response.LoginResponse;
import com.jency.guardian.features.authentication.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

}
