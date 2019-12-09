package com.ocr.projet6.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

 public enum RoleEnum implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER;

    public String getAuthority() {
        return name();
    }
}


class MyAuthoritiesMapper implements GrantedAuthoritiesMapper {

    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<RoleEnum> roles = EnumSet.noneOf(RoleEnum.class);

        for (GrantedAuthority a: authorities) {
            if ("ADMIN".equals(a.getAuthority())) {
                roles.add(RoleEnum.ROLE_ADMIN);
            } else if ("USER".equals(a.getAuthority())) {
                roles.add(RoleEnum.ROLE_USER);
            }
        }

        return roles;
    }
}