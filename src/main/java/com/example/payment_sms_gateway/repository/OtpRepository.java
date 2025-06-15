package com.example.payment_sms_gateway.repository;

import com.example.payment_sms_gateway.model.Otp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends CrudRepository<Otp, String> {
}
