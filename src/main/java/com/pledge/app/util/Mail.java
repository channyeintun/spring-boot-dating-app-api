package com.pledge.app.util;

public class Mail {
	String fromMail;
	String toMail;
	String subject;
	String body;

	public Mail() {

	}

	public Mail(String fromMail, String toMail, String subject, String body) {
		this.fromMail = fromMail;
		this.toMail = toMail;
		this.subject = subject;
		this.body = body;
	}

	public String getFromMail() {
		return fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public String getToMail() {
		return toMail;
	}

	public void setToMail(String toMail) {
		this.toMail = toMail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
