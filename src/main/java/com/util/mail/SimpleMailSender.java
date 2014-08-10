package com.util.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class SimpleMailSender {
	public boolean sendTextMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份验证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getPoroperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份验证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());

		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);

		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件的发送者
			mailMessage.setFrom(from);
			// 创建邮件接收者地址，并设置到邮箱消息中
			// Address to=new InternetAddress(mailInfo.getToAddress());
			Address[] to = new Address[mailInfo.getToAddress().length];
			for (int i = 0; i < to.length; i++) {
				to[i] = new InternetAddress(mailInfo.getToAddress()[i]);
			}
			mailMessage.setRecipients(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 设置附件

			// 发送邮件
			Transport.send(mailMessage);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public boolean sendHtmlMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份验证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getPoroperties();
		// 如果需要身份验证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址
		try {
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址
			Address[] to = new Address[mailInfo.getToAddress().length];
			for (int i = 0; i < to.length; i++) {
				to[i] = new InternetAddress(mailInfo.getToAddress()[i]);
			}
			mailMessage.setRecipients(Message.RecipientType.TO, to);
			mailMessage.setSubject(mailInfo.getSubject());
			mailMessage.setSentDate(new Date());
			Multipart mainPart = new MimeMultipart();
			BodyPart html = new MimeBodyPart();
			html.setContent(mailInfo.getContent(), "text/html;charset=utf-8");
			mainPart.addBodyPart(html);

			if (!mailInfo.getAttachFileNames().isEmpty()) {
				Enumeration efile = mailInfo.getAttachFileNames().elements();
				while (efile.hasMoreElements()) {
					html = new MimeBodyPart();
					String filename = efile.nextElement().toString();
					FileDataSource fds = new FileDataSource(filename);
					html.setDataHandler(new DataHandler(fds));
					html.setFileName(MimeUtility.encodeText(fds.getName(),
							"utf-8", "B"));
					mainPart.addBodyPart(html);
				}
				mailInfo.getAttachFileNames().removeAllElements();
			}
			mailMessage.setContent(mainPart);
			Transport.send(mailMessage);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
