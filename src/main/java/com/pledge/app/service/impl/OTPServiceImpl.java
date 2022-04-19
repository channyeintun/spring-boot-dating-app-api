package com.pledge.app.service.impl;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import com.pledge.app.dao.OTPDAO;
import com.pledge.app.entity.OTP;
import com.pledge.app.entity.User;
import com.pledge.app.service.OTPService;
import com.pledge.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OTPServiceImpl implements OTPService {

	private final Logger logger = LoggerFactory.getLogger(OTPServiceImpl.class);

	@Autowired
	private OTPDAO otpDao;

	@Autowired
	private UserService userService;

	private final int EXPIRATION = 5;

	@Override
	public OTP createOTPToken(String token, User user) {
		OTP tokenFromUser = user.getOtp();

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.add(Calendar.MINUTE, EXPIRATION);
		if (tokenFromUser == null) {
			OTP unsavedToken = new OTP(token, user);
			unsavedToken.setExpiryDate(cal.getTime());
			return otpDao.save(unsavedToken);
		} else {
			tokenFromUser.setToken(token);
			tokenFromUser.setUser(user);
			tokenFromUser.setExpiryDate(cal.getTime());
			return otpDao.save(tokenFromUser);
		}
	}

	@Override
	public OTP getOTPTokenByUser(User user) {
		return userService.findByUsername(user.getUsername()).get().getOtp();
	}

	@Override
	public boolean validateOTPToken(String token, User user) {
		User savedUser = userService.findByUsername(user.getUsername()).get();
		logger.info("validated");
		Date date1=savedUser.getOtp().getExpiryDate();
		Date date2=Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
		return token.equals(savedUser.getOtp().getToken()) && (date1.compareTo(date2)>0);
	}

	@Override
	public String generateToken(int n) {
		byte[] array = new byte[256];
		new Random().nextBytes(array);

		String randomString = new String(array, Charset.forName("UTF-8"));

		StringBuffer strBuffer = new StringBuffer();

		for (int k = 0; k < randomString.length(); k++) {

			char ch = randomString.charAt(k);

			if (((ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (n > 0)) {

				strBuffer.append(ch);
				n--;
			}
		}

		return strBuffer.toString();
	}

}
