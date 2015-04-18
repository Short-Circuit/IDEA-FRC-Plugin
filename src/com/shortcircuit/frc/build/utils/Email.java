package com.shortcircuit.frc.build.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * @author ShortCircuit908
 */
public class Email {
	private static final String SMTP_SERVER = "smtp.gmail.com";
	private static final int SMTP_HOST_PORT = 587;
	public static final String APP_EMAIL = "frcpluginsolutions@gmail.com";
	private static final Properties GLOBAL_PROPS = System.getProperties();
	private static final Session MAIL_SESSION;

	static {
		GLOBAL_PROPS.setProperty("mail.smtp.starttls.enable", "true");
		GLOBAL_PROPS.setProperty("mail.smtp.host", SMTP_SERVER);
		GLOBAL_PROPS.setProperty("mail.smtp.user", APP_EMAIL);
		GLOBAL_PROPS.setProperty("mail.smtp.password", Encrypt.APP_PASS);
		GLOBAL_PROPS.setProperty("mail.smtp.port", SMTP_HOST_PORT + "");
		GLOBAL_PROPS.setProperty("mail.smtp.auth", "true");
		MAIL_SESSION = Session.getDefaultInstance(GLOBAL_PROPS);
	}

	public static void sendMessage(String header, String body) throws MessagingException {
		MimeMessage message = new MimeMessage(MAIL_SESSION);
		message.setText(body);
		message.setSubject(header);
		message.setSentDate(new Date());
		message.setFrom(APP_EMAIL);
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(APP_EMAIL));
		Transport transport = MAIL_SESSION.getTransport("smtp");
		transport.connect(SMTP_SERVER, APP_EMAIL, Encrypt.APP_PASS);
		transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
		transport.close();
	}
}
