package com.jency.guardian.features.authentication.dto.response;

public class VerifyEmailOtpResponse {

    private String message;

    public VerifyEmailOtpResponse() {
    }

    public VerifyEmailOtpResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
