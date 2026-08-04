package com.jency.guardian.features.authentication.dto.response;

public class LoginOtpVerifyResponse {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String message;

    public LoginOtpVerifyResponse(String accessToken,
                                  String tokenType,
                                  Long expiresIn,
                                  String message) {

        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String getMessage() {
        return message;
    }
}