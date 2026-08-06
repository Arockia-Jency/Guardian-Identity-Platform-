package com.jency.guardian.features.authentication.dto.response;

public class LogoutResponse {

    private String message;

    public LogoutResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}