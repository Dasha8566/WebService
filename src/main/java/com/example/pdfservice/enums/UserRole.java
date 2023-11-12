package com.example.pdfservice.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    SUPER_ADMIN(Set.of(UserPermission.SUPER_ADMIN_PERMISSION, UserPermission.ADMIN_PERMISSION, UserPermission.USER_PERMISSION)),
    ADMIN(Set.of(UserPermission.ADMIN_PERMISSION,UserPermission.USER_PERMISSION)),
    USER(Set.of(UserPermission.USER_PERMISSION));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
