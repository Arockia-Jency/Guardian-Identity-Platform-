
package com.jency.guardian.features.profile.service.impl;

import com.jency.guardian.common.exception.ApiException;
import com.jency.guardian.features.authentication.entity.User;
import com.jency.guardian.features.authentication.repository.UserRepository;
import com.jency.guardian.features.authenticator.repository.AuthenticatorDeviceRepository;
import com.jency.guardian.features.authenticator.repository.RecoveryCodeRepository;
import com.jency.guardian.features.profile.dto.request.ChangePasswordRequest;
import com.jency.guardian.features.profile.dto.request.DeleteAccountRequest;
import com.jency.guardian.features.profile.dto.request.UpdateProfileRequest;
import com.jency.guardian.features.profile.dto.response.ChangePasswordResponse;
import com.jency.guardian.features.profile.dto.response.DeleteAccountResponse;
import com.jency.guardian.features.profile.dto.response.UpdateProfileResponse;
import com.jency.guardian.features.profile.dto.response.ProfileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatorDeviceRepository deviceRepository;
    private final RecoveryCodeRepository recoveryCodeRepository;

    public ProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticatorDeviceRepository deviceRepository, RecoveryCodeRepository recoveryCodeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.deviceRepository = deviceRepository;
        this.recoveryCodeRepository = recoveryCodeRepository;
    }

    @Override
    public ProfileResponse getProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }

    @Override
    public UpdateProfileResponse updateProfile(UpdateProfileRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        user.setFullName(request.getFullName());

        userRepository.save(user);

        return new UpdateProfileResponse("Profile updated successfully");
    }

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPasswordHash())) {

            throw new ApiException("Current password is incorrect.");
        }

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new ApiException("New password and confirm password do not match.");
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPasswordHash())) {

            throw new ApiException("New password cannot be same as current password.");
        }

        user.setPasswordHash(
                passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return new ChangePasswordResponse(
                "Password changed successfully.");
    }

    @Override
    @Transactional
    public DeleteAccountResponse deleteAccount(DeleteAccountRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new ApiException("Invalid password.");
        }

        deviceRepository.deleteByUserId(user.getId());

        recoveryCodeRepository.deleteByUserId(user.getId());

        userRepository.delete(user);

        return new DeleteAccountResponse("Account deleted successfully.");
    }
}
