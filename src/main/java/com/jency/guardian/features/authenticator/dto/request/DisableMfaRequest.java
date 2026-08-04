package com.jency.guardian.features.authenticator.dto.request;

public class DisableMfaRequest {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}