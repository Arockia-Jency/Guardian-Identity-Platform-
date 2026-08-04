package com.jency.guardian.features.authenticator.repository;

import com.jency.guardian.features.authenticator.entity.RecoveryCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, Long> {

    List<RecoveryCode> findByUserId(Long userId);

    Optional<RecoveryCode> findByCodeAndUsedFalse(String code);

    void deleteByUserId(Long userId);
}