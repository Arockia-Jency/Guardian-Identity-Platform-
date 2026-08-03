package com.jency.guardian.security.service.impl;


import com.jency.guardian.security.service.SecretKeyService;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class SecretKeyServiceImpl implements SecretKeyService {

    @Override
    public String generateSecretKey() {

        byte[] buffer = new byte[20];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(buffer);

        Base32 base32 = new Base32();

        return base32.encodeToString(buffer).replace("=", "");
    }
}
