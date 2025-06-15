package com.example.payment_sms_gateway.repository;

import com.example.payment_sms_gateway.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentTransaction, UUID> {
    List<PaymentTransaction> findByUserId(UUID userId);

    List<PaymentTransaction> findAllByOrderByCreatedAtDesc();
}
