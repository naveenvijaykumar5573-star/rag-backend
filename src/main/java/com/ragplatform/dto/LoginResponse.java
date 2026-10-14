package com.ragplatform.dto;

public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private String userId;
    private String tenantId;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(
            String accessToken,
            String tokenType,
            String userId,
            String tenantId,
            String role
    ) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.userId = userId;
        this.tenantId = tenantId;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getUserId() {
        return userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getRole() {
        return role;
    }
}