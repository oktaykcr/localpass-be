package com.localpass.backend.model.user;

public enum RoleEnum {
    USER("User"), ADMIN("Admin");

    RoleEnum(String role) {
        this.role = role;
    }
    String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
