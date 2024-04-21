package com.ruhuna.controller;

import com.ruhuna.DTO.OTPValidationDTO;
import com.ruhuna.model.Customer;
import com.ruhuna.repository.CustomerRepository;
import com.ruhuna.services.EmailService;
import com.ruhuna.services.OTPAuthenticationService;
import com.ruhuna.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OTPAuthenticationService otpAuthenticationService;
    @GetMapping("/generate")

    public ResponseEntity<String> generateOtp(@RequestParam String phoneNumber) {
        if (customerRepository.findByMobileNumber(phoneNumber).size() > 0) {
            String otp = String.valueOf(otpService.generateOTP(phoneNumber));
            // Send OTP to the user via SMS or email
            Customer customer = (Customer) customerRepository.findByMobileNumber(phoneNumber).get(0);
            emailService.sendHtmlEmail(customer.getEmail(), "OTP", otp);

            return ResponseEntity.ok("OTP generated and sent to " + phoneNumber);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not registered with above mobile number");

    }

    // Endpoint to validate OTP
    @PostMapping("/validate")
    public String validateOtp(@RequestBody OTPValidationDTO otpValidationDTO) {
        String phoneNumber = otpValidationDTO.getPhoneNumber();
        String otp = otpValidationDTO.getOtp();
        //String storedOtp = String.valueOf(otpService.getOtp(phoneNumber));

        // OTP is valid
        Authentication authentication = otpAuthenticationService.authenticate(
                User.builder()
                        .username(otpValidationDTO.getPhoneNumber())
                        .password(otpValidationDTO.getOtp())
                        .build()
        );
        if (authentication == null) {
            throw new BadCredentialsException("Bad credentials!");
        }
        // Remove OTP from the map after successful validation
        otpService.clearOTP(phoneNumber);
        return "OTP validation successful";
    }

}