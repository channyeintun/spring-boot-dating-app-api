package com.pledge.app.repository.readWrite;

import com.pledge.app.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPReadWriteRepository extends JpaRepository<OTP,Long> {
}
