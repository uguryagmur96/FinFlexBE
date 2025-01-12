package com.finflex.service.impl;

import com.finflex.dto.ForgotMailRequest;
import com.finflex.dto.RegisterMailRequest;
import com.finflex.entity.Mail;
import com.finflex.exception.MailCouldNotSentException;
import com.finflex.mapper.MailMapper;
import com.finflex.repository.IMailRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {
    @Mock
    private IMailRepository mailRepository;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private MailMapper mailMapper;
    @Mock
    private Configuration freeMarkerConfig;
    @InjectMocks
    private MailServiceImpl mailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mailService, "frontendAddress", "testUrl");
        ReflectionTestUtils.setField(mailService, "registerMailTemplateName", "register_template.ftl");
        ReflectionTestUtils.setField(mailService, "registerMailSubject", "testRegisterSubject");
        ReflectionTestUtils.setField(mailService, "resetPasswordMailTemplateName", "forgot_template.ftl");
        ReflectionTestUtils.setField(mailService, "resetPasswordMailSubject", "testPasswordSubject");
    }

    @Test
    void test_send_register_mail_success() throws IOException, TemplateException {
        RegisterMailRequest request = new RegisterMailRequest();
        request.setToken("testToken");
        request.setSentToMailAddress("testMail");
        request.setUsername("testUser");
        request.setPassword("testPassword");

        Mail mail = new Mail();
        mail.setToken("testToken");
        mail.setSentToMailAddress("testMail");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        Template template = mock(Template.class);

        when(mailMapper.mapRegisterMailToMail(request)).thenReturn(mail);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freeMarkerConfig.getTemplate("register_template.ftl")).thenReturn(template);

        mailService.sendRegisterMail(request);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
        verify(mailRepository, times(1)).save(any(Mail.class));
    }

    @Test
    void test_send_forgot_mail_success() throws IOException {
        ForgotMailRequest request = new ForgotMailRequest();
        request.setToken("testToken");
        request.setSentToMailAddress("testMail");

        Mail mail = new Mail();
        mail.setToken("testToken");
        mail.setSentToMailAddress("testMail");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        Template template = mock(Template.class);

        when(mailMapper.mapRegisterMailToMail(request)).thenReturn(mail);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freeMarkerConfig.getTemplate("forgot_template.ftl")).thenReturn(template);

        mailService.sendForgotMail(request);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
        verify(mailRepository, times(1)).save(any(Mail.class));
    }

    @Test
    void test_send_register_mail_template_not_found() throws IOException {
        RegisterMailRequest request = new RegisterMailRequest();
        request.setToken("testToken");
        request.setSentToMailAddress("testMail");
        request.setUsername("testUser");
        request.setPassword("testPassword");

        Mail mail = new Mail();
        mail.setToken("testToken");
        mail.setSentToMailAddress("testMail");

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailMapper.mapRegisterMailToMail(request)).thenReturn(mail);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freeMarkerConfig.getTemplate("register_template.ftl")).thenThrow(new FileNotFoundException("Template not found"));

        assertThrows(MailCouldNotSentException.class, () -> mailService.sendRegisterMail(request));
    }
}