package com.jency.guardian.features.authentication.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String to, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Guardian - Email Verification OTP");

        message.setText("""
                Hello,

                Your Guardian Email Verification OTP is:

                %s

                This OTP is valid for 5 minutes.

                Thank you,
                Guardian Team
                """.formatted(otp));

        mailSender.send(message);
    }
}
