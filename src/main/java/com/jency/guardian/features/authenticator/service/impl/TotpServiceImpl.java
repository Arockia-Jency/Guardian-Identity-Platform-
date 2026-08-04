package com.jency.guardian.features.authenticator.service.impl;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.time.SystemTimeProvider;
import org.springframework.stereotype.Service;

@Service
public class TotpServiceImpl implements TotpService {

    @Override
    public boolean verify(String secretKey, String otp) {

        System.out.println("========== TOTP VERIFY ==========");
        System.out.println("Secret Key : " + secretKey);
        System.out.println("OTP From Request : [" + otp + "]");
        System.out.println("OTP Length : " + otp.length());

        CodeVerifier verifier = new DefaultCodeVerifier(
                new DefaultCodeGenerator(HashingAlgorithm.SHA1),
                new SystemTimeProvider()
        );

        boolean result = verifier.isValidCode(secretKey, otp.trim());

        System.out.println("Verification Result : " + result);
        System.out.println("=================================");

        return result;
    }
}
