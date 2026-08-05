package com.jency.guardian.features.authentication.dto.response;

public class VerifyEmailResponse {

    private String message;

    public VerifyEmailResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
