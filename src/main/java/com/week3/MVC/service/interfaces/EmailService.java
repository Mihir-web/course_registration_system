package com.week3.MVC.service.interfaces;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String body);
}
