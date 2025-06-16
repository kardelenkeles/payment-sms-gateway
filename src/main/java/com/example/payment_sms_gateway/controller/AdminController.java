package com.example.payment_sms_gateway.controller;

import com.example.payment_sms_gateway.model.PaymentTransaction;
import com.example.payment_sms_gateway.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentTransaction>> getAllTransactions() {
        return ResponseEntity.ok(paymentService.getAllTransactions());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentTransaction>> getUserTransactions(@PathVariable String userId) {
        return ResponseEntity.ok(paymentService.getUserTransactions(userId));
    }
}