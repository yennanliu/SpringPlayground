package com.yen.springBootAdvance3;

// https://www.youtube.com/watch?v=s-IYp4L1Reo&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=26

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class SpringBootAdvance3ApplicationTests {

	@Autowired
	JavaMailSender javaMailSender;

	@Test
	void contextLoads() {
	}

	@Test
	public void testSendEmail(){

		SimpleMailMessage message = new SimpleMailMessage();

		message.setSubject("today is holiday!!!");
		message.setText("ho ho ho enjoy ur day");
		message.setTo("test@google.com");
		message.setFrom("admin@google.com");

		javaMailSender.send(message);
	}

	@Test
	public void testSendEmail2() throws MessagingException {

		// step 1) create complex mail instance
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper= new MimeMessageHelper(mimeMessage, true);

		// step 2) setting
		helper.setSubject("today is holiday!!!");
		helper.setText("<b>Subject : </b>", true);
		helper.setText("ho ho ho enjoy ur day");
		helper.setTo("test@google.com");
		helper.setFrom("admin@google.com");

		// step 3) upload file
		helper.addAttachment("test.txt", new File("springBootAdvance3/README.md"));
		helper.addAttachment("test2.txt", new File("springBootAdvance3/pom.xml"));

		javaMailSender.send(mimeMessage);
	}

}
