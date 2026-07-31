package com.jency.guardian.authentication.dto.response;

public class LoginResponse {

    private Long userId;
    private String fullName;
    private String message;

    public LoginResponse(Long userId, String fullName, String message) {
        this.userId = userId;
        this.fullName = fullName;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMessage() {
        return message;
    }
}