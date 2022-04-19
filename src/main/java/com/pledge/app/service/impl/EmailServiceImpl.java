package com.pledge.app.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.pledge.app.service.EmailService;
import com.pledge.app.util.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendMail(Mail mail) throws MailException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(mail.getFromMail());
		helper.setTo(mail.getToMail());
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(), true);
		mailSender.send(message);
	}

}
