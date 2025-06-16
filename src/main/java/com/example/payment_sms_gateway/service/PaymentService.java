package com.example.payment_sms_gateway.service;

import com.example.payment_sms_gateway.dto.PaymentRequest;
import com.example.payment_sms_gateway.dto.PaymentResponse;
import com.example.payment_sms_gateway.exception.PaymentException;
import com.example.payment_sms_gateway.model.PaymentTransaction;
import com.example.payment_sms_gateway.model.User;
import com.example.payment_sms_gateway.repository.jpa.PaymentRepository;
import com.example.payment_sms_gateway.repository.jpa.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final SmsService smsService;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;


    @Transactional
    public PaymentResponse processPayment(PaymentRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PaymentException("User not found"));

        Stripe.apiKey = stripeSecretKey;

        try {
            PaymentIntent intent = PaymentIntent.create(
                    PaymentIntentCreateParams.builder()
                            .setAmount(request.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                            .setCurrency(request.getCurrency().toLowerCase())
                            .build()
            );

            PaymentTransaction transaction = new PaymentTransaction();
            transaction.setAmount(request.getAmount());
            transaction.setCurrency(request.getCurrency());
            transaction.setStatus("COMPLETED");
            transaction.setStripePaymentId(intent.getId());
            transaction.setUser(user);
            transaction.setDescription(request.getDescription());
            paymentRepository.save(transaction);

            String otp = otpService.generateOtp(transaction.getId().toString());
            smsService.sendSms(user.getPhone(), "Your access code is: " + otp);

            return new PaymentResponse(
                    transaction.getId().toString(),
                    "COMPLETED",
                    "Payment processed successfully",
                    Instant.now(),
                    otp
            );
        } catch (StripeException e) {
            throw new PaymentException("Payment failed: " + e.getMessage());
        }
    }

    public List<PaymentTransaction> getAllTransactions() {
        return paymentRepository.findAll();
    }

    public List<PaymentTransaction> getUserTransactions(String userId) {
        UUID uuid = UUID.fromString(userId);
        return paymentRepository.findAllByUserId(uuid);
    }


    public PaymentResponse getPaymentStatus(String transactionId, String username) {
        PaymentTransaction transaction = paymentRepository.findById(UUID.fromString(transactionId))
                .orElseThrow(() -> new PaymentException("Transaction not found"));

        if (!transaction.getUser().getUsername().equals(username)) {
            throw new PaymentException("Unauthorized access to transaction");
        }

        return new PaymentResponse(
                transaction.getId().toString(),
                transaction.getStatus(),
                "Payment status retrieved",
                transaction.getCreatedAt(),
                null
        );
    }


}