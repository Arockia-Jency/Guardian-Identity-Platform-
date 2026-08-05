package com.jency.guardian.features.profile.dto.response;


public class ChangePasswordResponse {

    private String message;

    public ChangePasswordResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}