package com.jency.guardian.features.authenticator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DisableMfaRequest {

    @NotBlank(message = "Password is required")
    @Size(min = 8,message = "Password must be at least 8 characters")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}