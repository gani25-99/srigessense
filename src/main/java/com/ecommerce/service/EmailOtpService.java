package com.ecommerce.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailOtpService {

    @Autowired
    private EmailService emailService;

    // Stores OTP temporarily
    private final Map<String, String> otpStorage = new HashMap<>();

    // ==========================
    // Send Email OTP
    // ==========================
    public void sendOtp(String email) {

        String otp = String.format("%06d", new Random().nextInt(1000000));

        otpStorage.put(email, otp);

        String subject = "SRIG ESSENSE - Login OTP";

        String body =
                "Dear Customer,\n\n"
              + "Your Login OTP is: " + otp + "\n\n"
              + "This OTP is valid for 5 minutes.\n\n"
              + "Do not share this OTP with anyone.\n\n"
              + "Regards,\n"
              + "SRIG ESSENSE";

        emailService.sendEmail(email, subject, body);

        System.out.println("Email OTP : " + otp);
    }

    // ==========================
    // Verify Email OTP
    // ==========================
    public boolean verifyOtp(String email, String otp) {

        String savedOtp = otpStorage.get(email);

        if (savedOtp == null) {
            return false;
        }

        if (!savedOtp.equals(otp)) {
            return false;
        }

        otpStorage.remove(email);

        return true;
    }

}