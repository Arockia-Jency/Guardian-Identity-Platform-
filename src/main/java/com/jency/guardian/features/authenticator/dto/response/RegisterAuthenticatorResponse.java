package com.jency.guardian.features.authenticator.dto.response;


public class RegisterAuthenticatorResponse {

    private String secretKey;

    public RegisterAuthenticatorResponse(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
