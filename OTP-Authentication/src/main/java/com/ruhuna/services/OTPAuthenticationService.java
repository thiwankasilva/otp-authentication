package com.ruhuna.services;

import com.ruhuna.configs.OTPAuthenticationToken;
import com.ruhuna.configs.providers.OTPAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class OTPAuthenticationService {

    @Autowired
    private OTPAuthenticationProvider otpAuthenticationProvider;

    public Authentication authenticate(UserDetails userDetails) {

        if (!userDetails.getPassword().isEmpty()) {
            return otpAuthenticationProvider.authenticate(
                    new OTPAuthenticationToken(userDetails.getUsername(), userDetails.getPassword())
            );


        }
        return null;
    }
}
