package com.jency.guardian.features.authentication.dto.request;


public class ResendEmailOtpRequest {

    private String email;

    public ResendEmailOtpRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
