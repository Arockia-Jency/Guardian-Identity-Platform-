package com.jency.guardian.features.authentication.dto.response;

public class VerifyResetOtpResponse {

    private String message;

    public VerifyResetOtpResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}