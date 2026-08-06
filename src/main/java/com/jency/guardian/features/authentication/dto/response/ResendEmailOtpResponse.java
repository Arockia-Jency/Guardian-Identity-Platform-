package com.jency.guardian.features.authentication.dto.response;

public class ResendEmailOtpResponse {

    private String message;

    public ResendEmailOtpResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
