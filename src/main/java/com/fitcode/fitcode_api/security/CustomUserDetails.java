package com.fitcode.fitcode_api.security;

import com.fitcode.fitcode_api.models.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;
    private final List<GrantedAuthority> authorities;

    public static CustomUserDetails fromUser(User user) {
        List<GrantedAuthority> auth = List.of(user.getRole())
                .stream()
                .filter(r -> r != null)
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
        return new CustomUserDetails(user, auth);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getDeletedAt() == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getDeletedAt() == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getDeletedAt() == null;
    }

    @Override
    public boolean isEnabled() {
        return user.getDeletedAt() == null;
    }
}
