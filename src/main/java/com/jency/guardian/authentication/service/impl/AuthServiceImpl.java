package com.jency.guardian.authentication.service.impl;

import com.jency.guardian.authentication.dto.request.LoginRequest;
import com.jency.guardian.authentication.dto.request.RegisterRequest;
import com.jency.guardian.authentication.dto.response.LoginResponse;
import com.jency.guardian.authentication.dto.response.RegisterResponse;
import com.jency.guardian.authentication.entity.User;
import com.jency.guardian.authentication.repository.UserRepository;
import com.jency.guardian.authentication.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import com.jency.guardian.security.service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService
                           ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                "User registered successfully"
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                "Bearer",
                3600000L
        );
    }
}