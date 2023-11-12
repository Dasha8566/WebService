package com.example.pdfservice.enums;

public enum UserPermission {

    USER_PERMISSION("user_permission"),
    ADMIN_PERMISSION("admin_permission"),
    SUPER_ADMIN_PERMISSION("super_admin_permission");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
