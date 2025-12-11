package com.infrafix.citizen_reporting.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Mode pengiriman email (REAL = kirim ke user, FAKE = masuk inbox dev)
    @Value("${app.mail.mode:REAL}")
    private String mailMode;

    @Value("${app.mail.dev-inbox:devinbox@yourdomain.com}")
    private String devInbox;

    @Value("${app.baseurl:https://gentle-meringue-9613a6.netlify.app}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String email, String token, String recipientName) {

        // VALIDASI EMAIL
        if (!com.infrafix.citizen_reporting.dto.validation.ValEmailValidatorDTO.isValidEmail(email)) {
            throw new RuntimeException("Email anda belum terdaftar atau tidak valid!");
        }

        try {
            String destination = email;

            // Mailtrap-like simulation
            if (mailMode.equalsIgnoreCase("FAKE")) {
                destination = devInbox;
            }

            String verifyUrl = baseUrl + "/auth/verify-email?token=" + token;

            // Load HTML template correctly from resources inside JAR
            ClassPathResource resource = new ClassPathResource("templates/email_verification.html");
            String html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Replace placeholders {{name}} {{verificationUrl}}
            html = html.replace("{{recipientName}}", recipientName)
                    .replace("{{verificationUrl}}", verifyUrl);

            // Prepare email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setTo(destination);
            helper.setSubject("Email Verification | INFRAFIX");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
