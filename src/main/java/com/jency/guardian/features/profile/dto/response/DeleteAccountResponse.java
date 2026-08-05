package com.jency.guardian.features.profile.dto.response;


public class DeleteAccountResponse {

    private String message;

    public DeleteAccountResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
