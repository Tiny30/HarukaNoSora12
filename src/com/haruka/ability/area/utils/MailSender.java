package com.haruka.ability.area.utils;

import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	private String server;
	private String port;
	private String userName;
	private String userPwd;
	private String formName;
	private String toName;
	private String context;
	private String subject;
	
	private MailSender(String server, String port, String userName, String userPwd, String formName, String toName,
			String context, String subject) {
		super();
		this.server = server;
		this.port = port;
		this.userName = userName;
		this.userPwd = userPwd;
		this.formName = formName;
		this.toName = toName;
		this.context = context;
		this.subject = subject;
	}

	public static class Builder {
		private String server;
		private String port;
		private String userName;
		private String userPwd;
		private String formName;
		private String toName;
		private StringBuffer context = new StringBuffer();
		private String subject;

		public Builder setServer(String server) {
			this.server = server;
			return this;
		}

		public Builder setPort(String port) {
			this.port = port;
			return this;
		}

		public Builder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public Builder setUserPwd(String userPwd) {
			this.userPwd = userPwd;
			return this;
		}

		public Builder setFormName(String formName) {
			this.formName = formName;
			return this;
		}

		public Builder setToName(String toName) {
			this.toName = toName;
			return this;
		}

		public Builder setContext(String context) {
			this.context.append(context);
			return this;
		}

		public Builder setSubject(String subject) {
			this.subject = subject;
			return this;
		}

		public MailSender create(){
			return new MailSender(server, port, userName, userPwd, formName, toName, context.toString(), subject) ;		
		}
	}

	public void sendMail() throws AddressException, MessagingException {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", server);
		props.setProperty("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, userPwd);
			}
		});
		
		

		// -- Create a new message --
		session.setDebug(true);
		Message msg = new MimeMessage(session);

		// -- Set the FROM and TO fields --
		msg.setFrom(new InternetAddress(userName));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toName, true));
		msg.setSubject(subject);
		msg.setText(context);
		msg.setSentDate(new Date());
		Transport transport = session.getTransport("smtp");

		transport.connect(server,  userPwd);
		// 发送
		transport.sendMessage(msg, msg.getAllRecipients());
//		Transport.send(msg);
		transport.close();
	}

	@Override
	public String toString() {
		return "MailSender [server=" + server + ", port=" + port + ", userName=" + userName + ", userPwd=" + userPwd
				+ ", formName=" + formName + ", toName=" + toName + ", context=" + context + ", subject=" + subject
				+ "]";
	}
	
	

}// 源代码片段来自云代码http://yuncode.net