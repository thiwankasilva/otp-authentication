package com.ruhuna.configs;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OTPAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public OTPAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public OTPAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
