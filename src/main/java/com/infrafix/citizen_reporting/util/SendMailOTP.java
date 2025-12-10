package com.infrafix.citizen_reporting.util;

import com.infrafix.citizen_reporting.security.JwtUtility;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.Files;

@Component
public class SendMailOTP {

    private final JavaMailSender mailSender;
    private final JwtUtility jwtUtility;

    public SendMailOTP(JavaMailSender mailSender, JwtUtility jwtUtility) {
        this.mailSender = mailSender;
        this.jwtUtility = jwtUtility;
    }

    public void verifyRegisOTP(String subject, String recipientEmail, String token, String verificationUrl, String templatePath) {
        try {
            String recipientName = GetName(token);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject(subject);

            // Load HTML template
            File templateFile = new File(templatePath);
            String htmlContent = new String(Files.readAllBytes(templateFile.toPath()));

            // Replace placeholders
            htmlContent = htmlContent.replace("{{recipientName}}", recipientName);
            htmlContent = htmlContent.replace("{{verificationUrl}}", verificationUrl);

            helper.setText(htmlContent, false);

            mailSender.send(message);
        } catch (Exception e) {
            LoggingFile.logException("SendMailOTP", "verifyRegisOTP", e);
        }
    }

    public String GetName(String token) {
        return (String) jwtUtility.getAllClaimsFromToken(token).get("name");
    }
}
