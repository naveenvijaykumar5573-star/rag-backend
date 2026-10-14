package com.ragplatform.dto;

import java.util.UUID;

public class RegisterResponse {

    private UUID userId;
    private UUID tenantId;
    private String name;
    private String email;
    private String tenantName;

    public RegisterResponse() {
    }

    public RegisterResponse(
            UUID userId,
            UUID tenantId,
            String name,
            String email,
            String tenantName
    ) {
        this.userId = userId;
        this.tenantId = tenantId;
        this.name = name;
        this.email = email;
        this.tenantName = tenantName;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTenantName() {
        return tenantName;
    }
}