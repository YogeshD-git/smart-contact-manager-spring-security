package com.smartcontactmanager.service;

 import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	public boolean sendEmail(String subject,String message,String recipient)
	{
		boolean f= false;

		String sender = "randomyogesh@gmail.com";

		String host = "smtp.gmail.com";
		String pass = "RandomYogesh@12";

		//get the sys prop
		Properties prop = System.getProperties();
		
		prop.put("mail.smtp.host",host);
		prop.put("mail.smtp.port","465");
		prop.put("mail.smtp.ssl.enable","true");
		prop.put("mail.smtp.auth","true");

		Session session = Session.getInstance(prop, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(sender ,pass );
				}
			});

		session.setDebug(true);
		
		MimeMessage msg = new MimeMessage(session);

		try{
			msg.setFrom(sender);

			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			
			msg.setSubject(subject);
			
			//msg.setText(message);
			msg.setContent(message,"text/html");

			Transport.send(msg);

			f=true;
			}
	catch(Exception e)
			{
				e.printStackTrace();
			}
	return f;

				
	}

}
