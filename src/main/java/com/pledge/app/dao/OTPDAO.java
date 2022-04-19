package com.pledge.app.dao;

import com.pledge.app.entity.OTP;
import com.pledge.app.repository.readOnly.OTPReadOnlyRepository;
import com.pledge.app.repository.readWrite.OTPReadWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OTPDAO {
    private OTPReadOnlyRepository otpReadOnlyRepository;
    private OTPReadWriteRepository otpReadWriteRepository;

    @Autowired
    public OTPDAO(OTPReadWriteRepository otpReadWriteRepository,
                  OTPReadOnlyRepository otpReadOnlyRepository) {
        this.otpReadOnlyRepository = otpReadOnlyRepository;
        this.otpReadWriteRepository = otpReadWriteRepository;
    }

    public OTP save(OTP otp) {
        return this.otpReadWriteRepository.saveAndFlush(otp);
    }
}
