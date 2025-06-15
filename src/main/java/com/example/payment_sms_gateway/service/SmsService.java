package com.example.payment_sms_gateway.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromNumber;

    @Async
    public void sendSms(String toNumber, String body) {
        try {
            Twilio.init(accountSid, authToken);
            Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    body
            ).create();
            log.info("SMS sent to {}", toNumber);
        } catch (Exception e) {
            log.error("Failed to send SMS: {}", e.getMessage());
            throw new RuntimeException("SMS gönderimi başarısız oldu");
        }
    }
}