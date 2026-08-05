package com.jency.guardian.features.authentication.repository;

import com.jency.guardian.features.authentication.entity.EmailVerificationOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationOtpRepository
        extends JpaRepository<EmailVerificationOtp, Long> {

    Optional<EmailVerificationOtp> findByUserId(Long userId);

    Optional<EmailVerificationOtp> findByUserIdAndOtpAndVerifiedFalse(
            Long userId,
            String otp
    );

    void deleteByUserId(Long userId);
}
