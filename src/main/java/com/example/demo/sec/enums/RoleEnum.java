package com.example.demo.sec.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.example.demo.sec.enums.PermissionEnum.*;

@RequiredArgsConstructor
public enum RoleEnum {
    SUPERADMIN(
            Set.of(
                    SUPERADMIN_CREATE,
                    SUPERADMIN_READ,
                    SUPERADMIN_UPDATE,
                    SUPERADMIN_DELETE,
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    ),
    USER(Collections.emptySet()),
    GUEST(Collections.emptySet());

    @Getter
    private final Set<PermissionEnum> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.name())).toList();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
