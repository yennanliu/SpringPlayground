package com.yen.springBootAdvance3;

// https://www.youtube.com/watch?v=s-IYp4L1Reo&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=26

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

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
	public void testSendEmail2(){

		// step 1) create complex mail instance
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		SimpleMailMessage message = new SimpleMailMessage();

		message.setSubject("today is holiday!!!");
		message.setText("ho ho ho enjoy ur day");
		message.setTo("test@google.com");
		message.setFrom("admin@google.com");

		javaMailSender.send(message);
	}

}
