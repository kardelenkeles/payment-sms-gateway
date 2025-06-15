package com.example.payment_sms_gateway.service;


import com.example.payment_sms_gateway.dto.PaymentRequest;
import com.example.payment_sms_gateway.dto.PaymentResponse;
import com.example.payment_sms_gateway.exception.PaymentException;
import com.example.payment_sms_gateway.model.PaymentTransaction;
import com.example.payment_sms_gateway.model.User;
import com.example.payment_sms_gateway.repository.PaymentRepository;
import com.example.payment_sms_gateway.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final SmsService smsService;

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PaymentException("User not found"));

        Stripe.apiKey = stripeSecretKey;
        PaymentIntent intent = null;
        try {
            intent = PaymentIntent.create(
                    new PaymentIntentCreateParams.Builder()
                            .setAmount(request.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                            .setCurrency(request.getCurrency().toLowerCase())
                            .build()
            );
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

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
    }

    public boolean validateOtp(String transactionId, String otp) {
        return true;
    }
}