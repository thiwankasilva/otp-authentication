package com.ruhuna.configs.providers;

import com.ruhuna.configs.OTPAuthenticationToken;
import com.ruhuna.model.Customer;
import com.ruhuna.model.Role;
import com.ruhuna.repository.CustomerRepository;
import com.ruhuna.services.OTPAuthenticationService;
import com.ruhuna.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class OTPAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OTPService otpService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        Customer customer = customerRepository.findByMobileNumber(username).get(0);

        if (customer.getId()>0) {

            if (otpService.getOtp(username) != 0) {
                if (passwordEncoder.matches(password, String.valueOf(otpService.getOtp(username)))) {
                    return new OTPAuthenticationToken(
                            username,
                            null,
                            getGrantedAuthorities(customer.getAuthorities())
                    );
                }
            }
            else
            {
                throw new BadCredentialsException("OTP Expired");
            }
        }
            throw new UsernameNotFoundException("Username not found: " + username);
    }
    private List<GrantedAuthority> getGrantedAuthorities(Set<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return grantedAuthorities;
    }
    @Override
    public boolean supports(Class<?> authentication) {
         return authentication.equals(OTPAuthenticationToken.class);
    }
}



