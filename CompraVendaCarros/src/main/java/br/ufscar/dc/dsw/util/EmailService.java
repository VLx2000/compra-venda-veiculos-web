package br.ufscar.dc.dsw.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.ufscar.dc.dsw.controller.PropostaController;

public class EmailService {

	public void send(InternetAddress from, InternetAddress to, String subject, String body, File file) {

		try {

			Properties prop = new Properties();
			InputStream is = PropostaController.class.getClassLoader().getResourceAsStream("config.properties");

			if (is != null) {
				prop.load(is);
			} else {
				throw new FileNotFoundException("config.properties not found in the classpath");
			}
/*
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
*/
			Session session = Session.getInstance(prop, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("37504cfc6f8c8a", "f51c92ac4ed207");	//usuario e senha
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(from);
			message.setRecipient(Message.RecipientType.TO, to);
			message.setSubject(subject);

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(body, "text/plain");
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			
			if (file != null) {
				MimeBodyPart attachmentBodyPart = new MimeBodyPart();
				attachmentBodyPart.attachFile(file);
				multipart.addBodyPart(attachmentBodyPart);	
			}
			
			message.setContent(multipart);
			Transport.send(message);
			
			System.out.println("Mensagem enviada com sucesso!");
			
		} catch (Exception e) {
			System.out.println("Mensagem não enviada!");
			e.printStackTrace();
		}
	}
	
	public void send(InternetAddress from, InternetAddress to, String subject, String body) {
		send(from, to, subject, body, null);
	}
}