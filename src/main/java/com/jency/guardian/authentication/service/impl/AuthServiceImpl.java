package com.jency.guardian.authentication.service.impl;

import com.jency.guardian.authentication.dto.request.LoginRequest;
import com.jency.guardian.authentication.dto.request.RegisterRequest;
import com.jency.guardian.authentication.dto.response.LoginResponse;
import com.jency.guardian.authentication.dto.response.RegisterResponse;
import com.jency.guardian.authentication.entity.User;
import com.jency.guardian.authentication.repository.UserRepository;
import com.jency.guardian.authentication.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // TODO: Encrypt password using BCrypt
        user.setPasswordHash(request.getPassword());

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

        // For now, compare plain passwords.
        // Later we'll use BCrypt.
        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new LoginResponse(
                user.getId(),
                user.getFullName(),
                "Login successful"
        );
    }
}