package com.jency.guardian.features.authenticator.dto.request;


public class VerifyOtpRequest {

    private String otp;

    public VerifyOtpRequest() {
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}