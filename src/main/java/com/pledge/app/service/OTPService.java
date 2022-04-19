package com.pledge.app.service;


import com.pledge.app.entity.OTP;
import com.pledge.app.entity.User;

public interface OTPService {

	public OTP createOTPToken(String token, User user);

	public OTP getOTPTokenByUser(User user);

	public boolean validateOTPToken(String token, User user);
	
	public String generateToken(int n);
}
