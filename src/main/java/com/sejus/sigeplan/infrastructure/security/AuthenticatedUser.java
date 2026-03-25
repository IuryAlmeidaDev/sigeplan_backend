package com.sejus.sigeplan.infrastructure.security;

import com.sejus.sigeplan.domain.model.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class AuthenticatedUser implements UserDetails {

    private final String id;
    private final String cpf;
    private final String email;
    private final String fullName;
    private final String password;
    private final boolean active;
    private final Set<Role> roles;

    public AuthenticatedUser(
            String id,
            String cpf,
            String email,
            String fullName,
            String password,
            boolean active,
            Set<Role> roles
    ) {
        this.id = id;
        this.cpf = cpf;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.name()));

            if (role.permissions() != null) {
                role.permissions().forEach(permission ->
                        authorities.add(new SimpleGrantedAuthority(permission.name()))
                );
            }
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return cpf;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}