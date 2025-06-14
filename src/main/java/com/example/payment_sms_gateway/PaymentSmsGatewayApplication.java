package com.example.payment_sms_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAsync
public class PaymentSmsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentSmsGatewayApplication.class, args);
    }

}
