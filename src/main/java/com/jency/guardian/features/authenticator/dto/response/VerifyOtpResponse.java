package com.jency.guardian.features.authenticator.dto.response;


public class VerifyOtpResponse {

    private boolean verified;

    public VerifyOtpResponse(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }
}
