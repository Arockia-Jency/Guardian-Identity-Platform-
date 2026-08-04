package com.jency.guardian.features.authenticator.dto.response;


public class RegisterAuthenticatorResponse {

    private String secretKey;
    private String qrCode;

    public RegisterAuthenticatorResponse(String secretKey, String qrCode) {
        this.secretKey = secretKey;
        this.qrCode = qrCode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setSecretKey(String secretKey, String qrCode) {
        this.secretKey = secretKey;
        this.qrCode = qrCode;
    }
}
