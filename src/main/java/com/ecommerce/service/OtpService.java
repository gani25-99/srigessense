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

    public String sendOtp(String mobile) {
        return twilioVerifyService.sendOtp(mobile);
    }

    public String verifyOtp(String name,
                            String email,
                            String mobile,
                            String password,
                            String otp) {

        boolean verified = twilioVerifyService.verifyOtp(mobile, otp);

        if (!verified) {
            return "Invalid OTP";
        }

        Optional<User> user = userRepository.findByMobile(mobile);

        if (user.isEmpty()) {

            User newUser = new User();

            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setMobile(mobile);
            newUser.setPassword(password);

            userRepository.save(newUser);
        }

        return "Success";
    }

}