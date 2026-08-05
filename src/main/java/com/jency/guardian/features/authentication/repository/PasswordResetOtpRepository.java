package com.jency.guardian.features.authentication.repository;

import com.jency.guardian.features.authentication.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository
        extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    Optional<PasswordResetOtp> findByUserIdAndOtpAndVerifiedFalse(Long userId, String otp);

}
