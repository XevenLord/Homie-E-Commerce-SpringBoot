package com.example.demo.sec.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PermissionEnum {
    SUPERADMIN_READ("superadmin:read"),
    SUPERADMIN_UPDATE("superadmin:update"),
    SUPERADMIN_CREATE("superadmin:create"),
    SUPERADMIN_DELETE("superadmin:delete"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete");


    @Getter
    private final String permission;
}
