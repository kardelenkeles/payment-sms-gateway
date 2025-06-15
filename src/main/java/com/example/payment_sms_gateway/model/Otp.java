package com.example.payment_sms_gateway.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Otp", timeToLive = 300)
public class Otp {
    @Id
    private String transactionId;
    private String otpCode;

}