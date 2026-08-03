package com.jency.guardian.features.authenticator.service.impl;


import com.jency.guardian.features.authentication.entity.User;
import com.jency.guardian.features.authentication.repository.UserRepository;
import com.jency.guardian.features.authenticator.dto.request.RegisterAuthenticatorRequest;
import com.jency.guardian.features.authenticator.dto.response.RegisterAuthenticatorResponse;
import com.jency.guardian.features.authenticator.entity.AuthenticatorDevice;
import com.jency.guardian.features.authenticator.repository.AuthenticatorDeviceRepository;
import com.jency.guardian.security.service.SecretKeyService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatorServiceImpl implements AuthenticatorService {

    private final AuthenticatorDeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final SecretKeyService secretKeyService;

    public AuthenticatorServiceImpl(
            AuthenticatorDeviceRepository deviceRepository,
            UserRepository userRepository, SecretKeyService secretKeyService) {

        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.secretKeyService = secretKeyService;
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

        AuthenticatorDevice device = new AuthenticatorDevice();
        device.setUser(user);
        device.setDeviceName(request.getDeviceName());
        device.setSecretKey(secretKey);

        deviceRepository.save(device);

        return new RegisterAuthenticatorResponse(secretKey);
    }
}
