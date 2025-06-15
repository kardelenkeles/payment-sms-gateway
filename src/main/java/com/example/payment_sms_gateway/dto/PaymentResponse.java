
package com.example.payment_sms_gateway.dto;

import java.time.Instant;

public class PaymentResponse {
    private String transactionId;
    private String status;
    private String message;
    private Instant timestamp;
    private String otp;

    public PaymentResponse(String transactionId, String status, String message, Instant timestamp, String otp) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.otp = otp;
    }

    
}