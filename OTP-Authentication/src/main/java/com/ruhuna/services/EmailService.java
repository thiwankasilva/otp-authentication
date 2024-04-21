package com.ruhuna.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendHtmlEmail(String to, String subject, String otp) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            String htmlContent = getEmailHtmlContent(otp);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch ( jakarta.mail.MessagingException e) {
            // Handle exception
        }
    }

    private String getEmailHtmlContent(String otp) {
        String htmlContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>OTP Email</title>"
                + "<style>"
                + "body {"
                + "font-family: Arial, sans-serif;"
                + "margin: 0;"
                + "padding: 0;"
                + "background-color: #f5f5f5;"
                + "}"
                + ".container {"
                + "max-width: 600px;"
                + "margin: 20px auto;"
                + "padding: 20px;"
                + "background-color: #ffffff;"
                + "border-radius: 8px;"
                + "box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + "h1 {"
                + "color: #333333;"
                + "text-align: center;"
                + "}"
                + "p {"
                + "color: #666666;"
                + "text-align: center;"
                + "font-size: 18px;"
                + "}"
                + ".otp {"
                + "background-color: #f0f0f0;"
                + "text-align: center;"
                + "font-size: 24px;"
                + "padding: 10px;"
                + "border-radius: 8px;"
                + "margin-top: 20px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h1>OTP Email</h1>"
                + "<p>Your One-Time Password (OTP) for authentication is:</p>"
                + "<div class=\"otp\">" + otp + "</div>"
                + "<p>Please use this OTP to complete your action.</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        return htmlContent;
    }
}
