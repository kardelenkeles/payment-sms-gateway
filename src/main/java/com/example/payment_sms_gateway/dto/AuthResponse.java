package com.example.payment_sms_gateway.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    private String userId;
    private String message;

    public AuthResponse(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

}