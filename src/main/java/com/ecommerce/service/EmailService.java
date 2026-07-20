package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Normal Email (No Attachment)
    public void sendEmail(String to, String subject, String body) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            System.out.println("Email sent successfully.");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // Email With Invoice Attachment
    public void sendEmail(String to,
                          String subject,
                          String body,
                          byte[] invoice) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            if (invoice != null) {

                helper.addAttachment(
                        "Invoice.pdf",
                        new ByteArrayResource(invoice));

            }

            mailSender.send(message);

            System.out.println("Email with invoice sent successfully.");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}