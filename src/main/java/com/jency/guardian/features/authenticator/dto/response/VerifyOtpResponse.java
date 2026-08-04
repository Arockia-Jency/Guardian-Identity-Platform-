package com.jency.guardian.features.authenticator.dto.response;

public class VerifyOtpResponse {

    private boolean verified;
    private boolean mfaEnabled;
    private String message;

    public VerifyOtpResponse(boolean verified,
                             boolean mfaEnabled,
                             String message) {
        this.verified = verified;
        this.mfaEnabled = mfaEnabled;
        this.message = message;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public String getMessage() {
        return message;
    }
}