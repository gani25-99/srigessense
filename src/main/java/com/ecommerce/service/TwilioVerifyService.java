package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

import jakarta.annotation.PostConstruct;

@Service
public class TwilioVerifyService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.verify.service.sid}")
    private String serviceSid;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    // Send OTP
    public String sendOtp(String mobile) {

        Verification verification = Verification.creator(
                serviceSid,
                "+91" + mobile,
                "sms"
        ).create();

        System.out.println("Verification SID : " + verification.getSid());
        System.out.println("Verification Status : " + verification.getStatus());

        return "OTP Sent Successfully";
    }

    // Verify OTP
    public boolean verifyOtp(String mobile, String otp) {

        VerificationCheck verificationCheck =
                VerificationCheck.creator(serviceSid)
                        .setTo("+91" + mobile)
                        .setCode(otp)
                        .create();

        System.out.println("Verification Status : " + verificationCheck.getStatus());

        return "approved".equalsIgnoreCase(verificationCheck.getStatus());
    }
}