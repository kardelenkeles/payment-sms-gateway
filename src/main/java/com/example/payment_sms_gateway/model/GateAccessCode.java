package com.example.payment_sms_gateway.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "gate_access_codes")
public class GateAccessCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code;
    private Instant expiresAt;
    private boolean used;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private PaymentTransaction transaction;
}