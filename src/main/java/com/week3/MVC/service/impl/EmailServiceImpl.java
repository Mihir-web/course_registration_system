package com.week3.MVC.service.impl;

import com.week3.MVC.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            sendEmailAsync(toEmail, subject, body);
        } catch (Exception e) {
           log.error("Failed to send email. toEmail={}", toEmail);
           e.printStackTrace();
        }
    }


    private void sendEmailAsync(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Learning Kart <learning.kart.23@gmail.com>");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        log.info("Email sent Successfully. toEmail={}", toEmail);
    }

}
