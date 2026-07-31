package com.jency.guardian.authentication.service;

import com.jency.guardian.authentication.dto.request.LoginRequest;
import com.jency.guardian.authentication.dto.request.RegisterRequest;
import com.jency.guardian.authentication.dto.response.LoginResponse;
import com.jency.guardian.authentication.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

}
