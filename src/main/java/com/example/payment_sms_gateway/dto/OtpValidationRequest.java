package com.example.payment_sms_gateway.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OtpValidationRequest {
    private String transactionId;
    private String otp;

}