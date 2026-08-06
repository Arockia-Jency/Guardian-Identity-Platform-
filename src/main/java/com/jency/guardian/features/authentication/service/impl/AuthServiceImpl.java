package com.jency.guardian.features.authentication.service.impl;

import com.jency.guardian.common.exception.ApiException;
import com.jency.guardian.features.authentication.dto.request.*;
import com.jency.guardian.features.authentication.dto.response.*;
import com.jency.guardian.features.authentication.entity.EmailVerificationOtp;
import com.jency.guardian.features.authentication.entity.PasswordResetOtp;
import com.jency.guardian.features.authentication.entity.User;
import com.jency.guardian.features.authentication.repository.EmailVerificationOtpRepository;
import com.jency.guardian.features.authentication.repository.PasswordResetOtpRepository;
import com.jency.guardian.features.authentication.repository.UserRepository;
import com.jency.guardian.features.authentication.service.AuthService;
import com.jency.guardian.features.authentication.service.EmailService;
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
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticatorDeviceRepository deviceRepository;
    private final TotpService totpService;
    private final RecoveryCodeRepository recoveryCodeRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final EmailVerificationOtpRepository emailVerificationOtpRepository;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService, AuthenticatorDeviceRepository deviceRepository, TotpService totpService, RecoveryCodeRepository recoveryCodeRepository, PasswordResetOtpRepository passwordResetOtpRepository, EmailVerificationOtpRepository emailVerificationOtpRepository, EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.deviceRepository = deviceRepository;
        this.totpService = totpService;
        this.recoveryCodeRepository = recoveryCodeRepository;
        this.passwordResetOtpRepository = passwordResetOtpRepository;
        this.emailVerificationOtpRepository = emailVerificationOtpRepository;
        this.emailService = emailService;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        // Remove existing OTP if any
        emailVerificationOtpRepository.deleteByUserId(savedUser.getId());

// Generate 6-digit OTP
        String otp = String.valueOf(
                (int) ((Math.random() * 900000) + 100000));

        EmailVerificationOtp emailOtp =
                emailVerificationOtpRepository.findByUserId(savedUser.getId())
                        .orElse(new EmailVerificationOtp());

        emailOtp.setUser(savedUser);
        emailOtp.setOtp(otp);
        emailOtp.setVerified(false);
        emailOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        emailVerificationOtpRepository.save(emailOtp);

        emailService.sendOtp(savedUser.getEmail(), otp);

        return new RegisterResponse(
                savedUser.getId(),
                "User registered successfully. Please verify your email."
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            throw new ApiException("Invalid email or password");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ApiException("Invalid email or password");
        }

        // Add this block
        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new ApiException("Please verify your email before logging in.");
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
                .orElseThrow(() -> new ApiException("User not found"));

        if (!Boolean.TRUE.equals(user.getMfaEnabled())) {
            throw new ApiException("MFA is not enabled for this user.");
        }

        AuthenticatorDevice device = deviceRepository
                .findByUserId(user.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ApiException("Authenticator device not found."));

        boolean verified = totpService.verify(
                device.getSecretKey(),
                request.getOtp()
        );

        if (!verified) {
            throw new ApiException("Invalid OTP.");
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
                .orElseThrow(() -> new ApiException("User not found"));

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
    public LoginResponse verifyRecoveryCode(VerifyRecoveryCodeRequest request) {

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

    @Override
    @Transactional
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("User not found"));

        // Remove old OTP if it exists
        passwordResetOtpRepository.deleteByUserId(user.getId());

        // Generate 6-digit OTP
        String otp = String.valueOf(
                (int) ((Math.random() * 900000) + 100000));

        PasswordResetOtp passwordResetOtp =
                passwordResetOtpRepository.findByUserId(user.getId())
                        .orElse(new PasswordResetOtp());

        passwordResetOtp.setUser(user);
        passwordResetOtp.setOtp(otp);
        passwordResetOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        passwordResetOtp.setVerified(false);

        passwordResetOtpRepository.save(passwordResetOtp);

        passwordResetOtpRepository.save(passwordResetOtp);

       // Send OTP to email
        emailService.sendOtp(user.getEmail(), otp);

        return new ForgotPasswordResponse(
                "Password reset OTP has been sent to your email.");
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ApiException("User not found"));

        PasswordResetOtp passwordResetOtp =
                passwordResetOtpRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ApiException("Please verify OTP first"));

        if (!passwordResetOtp.getVerified()) {
            throw new ApiException("Please verify OTP first");
        }

        user.setPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        passwordResetOtpRepository.delete(passwordResetOtp);

        return new ResetPasswordResponse(
                "Password reset successfully."
        );
    }

    @Override
    public VerifyResetOtpResponse verifyResetOtp(VerifyResetOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ApiException("User not found"));

        PasswordResetOtp passwordResetOtp =
                passwordResetOtpRepository
                        .findByUserIdAndOtpAndVerifiedFalse(
                                user.getId(),
                                request.getOtp())
                        .orElseThrow(() ->
                                new ApiException("Invalid OTP"));

        if (passwordResetOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ApiException("OTP has expired");
        }

        passwordResetOtp.setVerified(true);
        passwordResetOtpRepository.save(passwordResetOtp);

        return new VerifyResetOtpResponse(
                "OTP verified successfully."
        );
    }

    @Override
    public VerifyEmailOtpResponse verifyEmailOtp(VerifyEmailOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ApiException("User not found"));

        EmailVerificationOtp emailOtp =
                emailVerificationOtpRepository
                        .findByUserIdAndOtpAndVerifiedFalse(
                                user.getId(),
                                request.getOtp())
                        .orElseThrow(() ->
                                new ApiException("Invalid OTP"));

        if (emailOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ApiException("OTP has expired");
        }

        user.setEmailVerified(true);
        userRepository.save(user);

        emailVerificationOtpRepository.delete(emailOtp);

        return new VerifyEmailOtpResponse(
                "Email verified successfully."
        );
    }

    @Override
    public ResendEmailOtpResponse resendEmailOtp(ResendEmailOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ApiException("User not found"));

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new ApiException("Email is already verified.");
        }

        String otp = String.valueOf(
                (int) ((Math.random() * 900000) + 100000));

        EmailVerificationOtp emailOtp =
                emailVerificationOtpRepository
                        .findByUserId(user.getId())
                        .orElse(new EmailVerificationOtp());

        emailOtp.setUser(user);
        emailOtp.setOtp(otp);
        emailOtp.setVerified(false);
        emailOtp.setExpiresAt(
                LocalDateTime.now().plusMinutes(5));

        emailVerificationOtpRepository.save(emailOtp);

        emailService.sendOtp(user.getEmail(), otp);

        return new ResendEmailOtpResponse(
                "A new verification OTP has been sent to your email.");
    }
}