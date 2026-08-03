package com.jency.guardian.features.authenticator.dto.request;

public class RegisterAuthenticatorRequest {

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
