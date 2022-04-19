package com.pledge.app.service;

import javax.mail.MessagingException;

import com.pledge.app.util.Mail;
import org.springframework.mail.MailException;

public interface EmailService {

	public void sendMail(Mail mail) throws MailException, MessagingException;
}
