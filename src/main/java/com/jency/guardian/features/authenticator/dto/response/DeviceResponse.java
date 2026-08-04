package com.jency.guardian.features.authenticator.dto.response;

import java.time.LocalDateTime;

public class DeviceResponse {

    private String deviceName;
    private String status;
    private LocalDateTime registeredAt;

    public DeviceResponse(String deviceName,
                          String status,
                          LocalDateTime registeredAt) {

        this.deviceName = deviceName;
        this.status = status;
        this.registeredAt = registeredAt;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
}