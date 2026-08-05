package com.jency.guardian.features.authentication.dto.response;

public class ForgotPasswordResponse {

    private String message;

    public ForgotPasswordResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}