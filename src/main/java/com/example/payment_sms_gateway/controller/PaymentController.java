package com.example.payment_sms_gateway.controller;

import com.example.payment_sms_gateway.dto.OtpValidationRequest;
import com.example.payment_sms_gateway.dto.PaymentRequest;
import com.example.payment_sms_gateway.dto.PaymentResponse;
import com.example.payment_sms_gateway.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestBody PaymentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                paymentService.processPayment(request, userDetails.getUsername())
        );
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOtp(
            @RequestBody OtpValidationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        boolean isValid = paymentService.validateOtp(
                request.getTransactionId(),
                request.getOtp()
        );

        if (isValid) {
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "OTP validated successfully"
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "status", "FAILED",
                "message", "Invalid OTP"
        ));
    }


}