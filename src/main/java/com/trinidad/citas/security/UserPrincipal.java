package com.trinidad.citas.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Extension de {@link User} de Spring Security que agrega el
 * ID del usuario para usarlo en auditoria y logs.
 */
@Getter
public class UserPrincipal extends User {

    private final Long id;
    private final String nombreCompleto;

    public UserPrincipal(Long id, String username, String password,
                         boolean enabled, boolean accountNonLocked,
                         Collection<? extends GrantedAuthority> authorities) {
        this(id, username, password, enabled, accountNonLocked, authorities, null);
    }

    public UserPrincipal(Long id, String username, String password,
                         boolean enabled, boolean accountNonLocked,
                         Collection<? extends GrantedAuthority> authorities,
                         String nombreCompleto) {
        super(username, password, enabled, true, true, accountNonLocked, authorities);
        this.id = id;
        this.nombreCompleto = nombreCompleto;
    }
}
