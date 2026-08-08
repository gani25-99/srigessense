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


    // =========================================================
    // NORMAL EMAIL
    // =========================================================

    public void sendEmail(
            String to,
            String subject,
            String body) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(to);

            message.setSubject(subject);

            message.setText(body);

            mailSender.send(message);

            System.out.println(
                    "Email sent successfully to : "
                            + to);

        } catch (Exception e) {

            System.err.println(
                    "Failed to send email to : "
                            + to);

            e.printStackTrace();
        }
    }


    // =========================================================
    // EMAIL WITH INVOICE PDF
    // =========================================================

    public void sendEmail(
            String to,
            String subject,
            String body,
            byte[] invoice) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();


            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true);


            helper.setTo(to);

            helper.setSubject(subject);

            helper.setText(body);


            // =================================================
            // ATTACH INVOICE
            // =================================================

            if (invoice != null &&
                    invoice.length > 0) {

                helper.addAttachment(
                        "SRIG_Invoice.pdf",
                        new ByteArrayResource(
                                invoice));

                System.out.println(
                        "Invoice attached successfully.");
            }


            // =================================================
            // SEND
            // =================================================

            mailSender.send(message);


            System.out.println(
                    "Email with invoice sent successfully to : "
                            + to);


        } catch (Exception e) {

            System.err.println(
                    "Failed to send invoice email to : "
                            + to);

            e.printStackTrace();
        }
    }
}