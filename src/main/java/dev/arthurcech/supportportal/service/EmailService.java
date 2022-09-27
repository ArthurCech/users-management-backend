package dev.arthurcech.supportportal.service;

import static dev.arthurcech.supportportal.constant.EmailConstant.CC_EMAIL;
import static dev.arthurcech.supportportal.constant.EmailConstant.DEFAULT_MESSAGE_NEW_PASSWORD;
import static dev.arthurcech.supportportal.constant.EmailConstant.DEFAULT_PORT_OUTLOOK;
import static dev.arthurcech.supportportal.constant.EmailConstant.EMAIL_SUBJECT;
import static dev.arthurcech.supportportal.constant.EmailConstant.FROM_EMAIL;
import static dev.arthurcech.supportportal.constant.EmailConstant.OUTLOOK_SMTP_SERVER;
import static dev.arthurcech.supportportal.constant.EmailConstant.PASSWORD;
import static dev.arthurcech.supportportal.constant.EmailConstant.SIMPLE_MAIL_TRANSFER_PROTOCOL_OUTLOOK;
import static dev.arthurcech.supportportal.constant.EmailConstant.SMTP_AUTH;
import static dev.arthurcech.supportportal.constant.EmailConstant.SMTP_HOST;
import static dev.arthurcech.supportportal.constant.EmailConstant.SMTP_PORT;
import static dev.arthurcech.supportportal.constant.EmailConstant.SMTP_STARTTLS_ENABLE;
import static dev.arthurcech.supportportal.constant.EmailConstant.SMTP_STARTTLS_REQUIRED;
import static dev.arthurcech.supportportal.constant.EmailConstant.USERNAME;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.sun.mail.smtp.SMTPTransport;

@Service
public class EmailService {

	public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
		Message message = createEmail(firstName, password, email);
		SMTPTransport smtpTransport = (SMTPTransport) getEmailSession()
				.getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL_OUTLOOK);
		smtpTransport.connect(OUTLOOK_SMTP_SERVER, USERNAME, PASSWORD);
		smtpTransport.sendMessage(message, message.getAllRecipients());
		smtpTransport.close();
	}

	private Message createEmail(String firstName, String password, String email) throws MessagingException {
		Message message = new MimeMessage(getEmailSession());
		message.setFrom(new InternetAddress(FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
		message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
		message.setSubject(EMAIL_SUBJECT);
		message.setText(String.format(DEFAULT_MESSAGE_NEW_PASSWORD, firstName, password));
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
	}

	private Session getEmailSession() {
		Properties properties = System.getProperties();
		properties.put(SMTP_HOST, OUTLOOK_SMTP_SERVER);
		properties.put(SMTP_AUTH, true);
		properties.put(SMTP_PORT, DEFAULT_PORT_OUTLOOK);
		properties.put(SMTP_STARTTLS_ENABLE, true);
		properties.put(SMTP_STARTTLS_REQUIRED, true);
		return Session.getInstance(properties, null);
	}

}
