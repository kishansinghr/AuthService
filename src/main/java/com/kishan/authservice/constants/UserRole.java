package com.kishan.authservice.constants;

public enum UserRole {
    ADMIN("admin"),
    CUSTOMER("customer"),
    SELLER("seller");

    private final String role;
    UserRole(String role) {
        this.role = role;
    }

    public String getName() {
        return this.role;
    }
}
