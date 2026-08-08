package com.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwilioVerifyService twilioVerifyService;

    // =====================================
    // SEND OTP
    // =====================================

    public String sendOtp(String mobile) {

        try {

            twilioVerifyService.sendOtp(mobile);

            return "OTP Sent";

        } catch (Exception e) {

            return e.getMessage();

        }

    }

    // =====================================
    // REGISTER OTP VERIFY
    // =====================================

    public String verifyOtp(
            String name,
            String email,
            String mobile,
            String password,
            String otp) {

        boolean verified = twilioVerifyService.verifyOtp(mobile, otp);

        if (!verified) {

            return "Invalid OTP";
        }

        Optional<User> existingUser =
                userRepository.findByMobile(mobile);

        User user;

        if (existingUser.isPresent()) {

            user = existingUser.get();

        } else {

            user = new User();

        }

        user.setName(name);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPassword(password);

        userRepository.save(user);

        return "Success";
    }

    // =====================================
    // LOGIN OTP VERIFY
    // =====================================

    public String verifyLoginOtp(
            String mobile,
            String otp) {

        boolean verified = twilioVerifyService.verifyOtp(mobile, otp);

        if (!verified) {

            return "Invalid OTP";
        }

        Optional<User> user =
                userRepository.findByMobile(mobile);

        if (user.isEmpty()) {

            return "User Not Found";
        }

        return "Success";
    }

}