package com.util.test;

import com.util.mail.MailSenderInfo;
import com.util.mail.SimpleMailSender;

public class JavaMailTest {
	public static void main(String[] args) {
		MailSenderInfo mailInfo = new MailSenderInfo();
		//mailInfo.setSsl(true);
		mailInfo.setMailServerHost("smtp.126.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("pb_2012@126.com");
		mailInfo.setPassword("198907242008");
		mailInfo.setFromAddress("pb_2012@126.com");
		mailInfo.setToAddress(new String[] { "2008pb1989@163.com" });
		mailInfo.setSubject("测试邮件主题");
		mailInfo.setContent("测试邮件内容");
		// mailInfo.addAttachFileNames("d:\\克里斯丁.xls");//增加附件
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendTextMail(mailInfo);// 发送文本格式
		sms.sendHtmlMail(mailInfo);// 发送html格式

	}
}
