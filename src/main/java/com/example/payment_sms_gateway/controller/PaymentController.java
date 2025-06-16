package com.example.payment_sms_gateway.controller;

import com.example.payment_sms_gateway.dto.PaymentRequest;
import com.example.payment_sms_gateway.dto.PaymentResponse;
import com.example.payment_sms_gateway.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestBody PaymentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(paymentService.processPayment(request, userDetails.getUsername()));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(
            @PathVariable String transactionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(paymentService.getPaymentStatus(transactionId, userDetails.getUsername()));
    }
}