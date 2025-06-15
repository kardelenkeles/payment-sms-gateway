package com.example.payment_sms_gateway.service;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;

    public OtpService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateOtp(String transactionId) {
        String otp = String.format("%06d", new Random().nextInt(999999));


        redisTemplate.opsForValue().set(
                "otp:" + transactionId,
                otp,
                Duration.ofMinutes(5)
        );

        return otp;
    }

    public boolean validateOtp(String transactionId, String otp) {
        String storedOtp = redisTemplate.opsForValue().get("otp:" + transactionId);
        return otp.equals(storedOtp);
    }
}