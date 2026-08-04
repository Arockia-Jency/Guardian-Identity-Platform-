package com.jency.guardian.features.authentication.service.impl;

import com.jency.guardian.features.authentication.dto.request.LoginOtpVerifyRequest;
import com.jency.guardian.features.authentication.dto.request.LoginRequest;
import com.jency.guardian.features.authentication.dto.request.RegisterRequest;
import com.jency.guardian.features.authentication.dto.response.LoginOtpVerifyResponse;
import com.jency.guardian.features.authentication.dto.response.LoginResponse;
import com.jency.guardian.features.authentication.dto.response.RegisterResponse;
import com.jency.guardian.features.authentication.entity.User;
import com.jency.guardian.features.authentication.repository.UserRepository;
import com.jency.guardian.features.authentication.service.AuthService;
import com.jency.guardian.features.authenticator.dto.request.VerifyRecoveryCodeRequest;
import com.jency.guardian.features.authenticator.dto.response.RecoveryCodesResponse;
import com.jency.guardian.features.authenticator.entity.AuthenticatorDevice;
import com.jency.guardian.features.authenticator.entity.RecoveryCode;
import com.jency.guardian.features.authenticator.repository.AuthenticatorDeviceRepository;
import com.jency.guardian.features.authenticator.repository.RecoveryCodeRepository;
import com.jency.guardian.features.authenticator.service.impl.TotpService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.jency.guardian.security.service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticatorDeviceRepository deviceRepository;
    private final TotpService totpService;
    private final RecoveryCodeRepository recoveryCodeRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService, AuthenticatorDeviceRepository deviceRepository, TotpService totpService, RecoveryCodeRepository recoveryCodeRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.deviceRepository = deviceRepository;
        this.totpService = totpService;
        this.recoveryCodeRepository = recoveryCodeRepository;
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
        if (Boolean.TRUE.equals(user.getMfaEnabled())) {

            return new LoginResponse(
                    true,
                    null,
                    null,
                    null,
                    "OTP verification required."
            );
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                false,
                token,
                "Bearer",
                3600000L,
                "Login successful."
        );
    }

    @Override
    public LoginOtpVerifyResponse verifyLoginOtp(LoginOtpVerifyRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(user.getMfaEnabled())) {
            throw new RuntimeException("MFA is not enabled for this user.");
        }

        AuthenticatorDevice device = deviceRepository
                .findByUserId(user.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Authenticator device not found."));

        boolean verified = totpService.verify(
                device.getSecretKey(),
                request.getOtp()
        );

        if (!verified) {
            throw new RuntimeException("Invalid OTP.");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginOtpVerifyResponse(
                token,
                "Bearer",
                3600000L,
                "Login successful."
        );
    }


    @Override
    public RecoveryCodesResponse generateRecoveryCodes() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        recoveryCodeRepository.deleteByUserId(user.getId());

        List<String> codes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            String code = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 10)
                    .toUpperCase();

            RecoveryCode recoveryCode = new RecoveryCode();
            recoveryCode.setUser(user);
            recoveryCode.setCode(code);

            recoveryCodeRepository.save(recoveryCode);

            codes.add(code);
        }

        return new RecoveryCodesResponse(codes);
    }

    @Override
    public LoginResponse verifyRecoveryCode(
            VerifyRecoveryCodeRequest request) {

        RecoveryCode recoveryCode = recoveryCodeRepository
                .findByCodeAndUsedFalse(request.getCode())
                .orElseThrow(() ->
                        new RuntimeException("Invalid recovery code"));

        recoveryCode.setUsed(true);
        recoveryCode.setUsedAt(LocalDateTime.now());

        recoveryCodeRepository.save(recoveryCode);

        User user = recoveryCode.getUser();

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                false,
                token,
                "Bearer",
                3600000L,
                "Login successful using recovery code."
        );
    }
}