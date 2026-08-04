package com.jency.guardian.features.authenticator.service.impl;


import com.jency.guardian.features.authentication.entity.User;
import com.jency.guardian.features.authentication.repository.UserRepository;
import com.jency.guardian.features.authenticator.dto.request.RegisterAuthenticatorRequest;
import com.jency.guardian.features.authenticator.dto.request.VerifyOtpRequest;
import com.jency.guardian.features.authenticator.dto.response.RegisterAuthenticatorResponse;
import com.jency.guardian.features.authenticator.dto.response.VerifyOtpResponse;
import com.jency.guardian.features.authenticator.entity.AuthenticatorDevice;
import com.jency.guardian.features.authenticator.repository.AuthenticatorDeviceRepository;
import com.jency.guardian.security.service.SecretKeyService;
import com.jency.guardian.security.service.impl.QrCodeServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatorServiceImpl implements AuthenticatorService {

    private final AuthenticatorDeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final SecretKeyService secretKeyService;
    private final QrCodeServiceImpl qrCodeService;
    private final TotpService totpService;

    public AuthenticatorServiceImpl(
            AuthenticatorDeviceRepository deviceRepository,
            UserRepository userRepository, SecretKeyService secretKeyService, QrCodeServiceImpl qrCodeService, TotpService totpService) {

        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.secretKeyService = secretKeyService;
        this.qrCodeService = qrCodeService;
        this.totpService = totpService;
    }

    @Override
    public RegisterAuthenticatorResponse register(RegisterAuthenticatorRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Temporary secret
        String secretKey = secretKeyService.generateSecretKey();
        String otpUri =
                "otpauth://totp/Guardian:" +
                        user.getEmail() +
                        "?secret=" +
                        secretKey +
                        "&issuer=Guardian";
        System.out.println("Generated Secret : " + secretKey);
        System.out.println("OTP URI : " + otpUri);
        String qrCode = qrCodeService.generateQrCode(otpUri);

        AuthenticatorDevice device = new AuthenticatorDevice();
        device.setUser(user);
        device.setDeviceName(request.getDeviceName());
        device.setSecretKey(secretKey);

        deviceRepository.save(device);

        return new RegisterAuthenticatorResponse(secretKey, qrCode);
    }

    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AuthenticatorDevice device = deviceRepository
                .findByUserId(user.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No authenticator registered"));

        System.out.println("DB Secret : " + device.getSecretKey());
        System.out.println("OTP Received : " + request.getOtp());

        boolean verified = totpService.verify(
                device.getSecretKey(),
                request.getOtp()
        );

        return new VerifyOtpResponse(verified);
    }
}
