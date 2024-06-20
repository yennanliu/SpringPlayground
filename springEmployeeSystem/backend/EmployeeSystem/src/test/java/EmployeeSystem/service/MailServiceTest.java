// package EmployeeSystem.service;
//
// import EmployeeSystem.model.NotificationEmail;
//
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.BeforeEach;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
//
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.mail.javamail.MimeMessagePreparator;
//
// import javax.mail.internet.MimeMessage;
// import java.util.*;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// class MailServiceTest {
//
//    @Mock
//    private JavaMailSender mailSender;
//
//    @Mock
//    private MailContentBuilder mailContentBuilder;
//
//    @InjectMocks
//    private MailService mailService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testSendMail() {
//
//        // Setup
//        NotificationEmail notificationEmail = new NotificationEmail();
//        notificationEmail.setRecipient("test@example.com");
//        notificationEmail.setSubject("Test Subject");
//        notificationEmail.setBody("Test Body");
//
//
// when(mailContentBuilder.build(notificationEmail.getBody())).thenReturn(notificationEmail.getBody());
//
//        // Test
//        mailService.sendMail(notificationEmail);
//
//        // Verification
//        verify(mailSender, times(1)).send(messagePreparator(notificationEmail));
//    }
//
//    // private help func
//    private MimeMessageHelper messagePreparator(NotificationEmail notificationEmail) {
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
//        final String adminEmail = "employee_admin@dev.com";
//        try {
//           messageHelper.setFrom(adminEmail);
//            messageHelper.setTo(notificationEmail.getRecipient());
//            messageHelper.setSubject(notificationEmail.getSubject());
//            messageHelper.setText(notificationEmail.getBody());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return messageHelper;
//    }
//
//
// }
