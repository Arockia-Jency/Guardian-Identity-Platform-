package com.jency.guardian.features.authenticator.repository;


import com.jency.guardian.features.authenticator.entity.AuthenticatorDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthenticatorDeviceRepository
        extends JpaRepository<AuthenticatorDevice, Long> {

    List<AuthenticatorDevice> findByUserId(Long userId);

}
