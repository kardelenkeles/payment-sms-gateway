package com.example.payment_sms_gateway.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentRequest {
    private BigDecimal amount;
    private String currency;
    private String description;

}