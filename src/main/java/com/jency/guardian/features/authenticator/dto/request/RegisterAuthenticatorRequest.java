package com.jency.guardian.features.authenticator.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RegisterAuthenticatorRequest {

    @NotBlank(message = "Device name is required")
    private String deviceName;

    public RegisterAuthenticatorRequest() {
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
