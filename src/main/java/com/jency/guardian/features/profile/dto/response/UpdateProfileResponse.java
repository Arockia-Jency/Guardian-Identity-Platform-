package com.jency.guardian.features.profile.dto.response;

public class UpdateProfileResponse {

    private String message;

    public UpdateProfileResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}