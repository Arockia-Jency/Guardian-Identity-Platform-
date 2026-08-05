package com.jency.guardian.features.authenticator.dto.request;

import jakarta.validation.constraints.NotBlank;

public class VerifyRecoveryCodeRequest {

    @NotBlank(message = "Recovery code is required")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}