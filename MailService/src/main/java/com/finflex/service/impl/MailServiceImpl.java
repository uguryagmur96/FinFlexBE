package com.finflex.service.impl;

import com.finflex.dto.ForgotMailRequest;
import com.finflex.dto.MailRequest;
import com.finflex.dto.RegisterMailRequest;
import com.finflex.entity.Mail;
import com.finflex.exception.ErrorType;
import com.finflex.exception.MailCouldNotSentException;
import com.finflex.mapper.MailMapper;
import com.finflex.repository.IMailRepository;
import com.finflex.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    @Value("${front-end.url}")
    private String frontendAddress;

    @Value("${front-end.register-mail-template.name}")
    private String registerMailTemplateName;

    @Value("${front-end.register-mail-template.subject}")
    private String registerMailSubject;

    @Value("${front-end.password-rest-mail-template.name}")
    private String resetPasswordMailTemplateName;

    @Value("${front-end.password-rest-mail-template.subject}")
    private String resetPasswordMailSubject;

    private final IMailRepository mailRepository;
    private final JavaMailSender javaMailSender;
    private final MailMapper mailMapper;
    private final Configuration freeMarkerConfig;

    @Override
    @KafkaListener(topics = "send-register-mail")
    public void sendRegisterMail(RegisterMailRequest mailRequest) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("link", createVerificationLink(mailRequest.getToken()));
        variables.put("username", mailRequest.getUsername());
        variables.put("password", mailRequest.getPassword());
        sendMail(mailRequest, variables, registerMailTemplateName, registerMailSubject);
    }

    @Override
    @KafkaListener(topics = "send-forgot-mail")
    public void sendForgotMail(ForgotMailRequest mailRequest) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("link", createResetPasswordLink(mailRequest.getToken()));
        sendMail(mailRequest, variables, resetPasswordMailTemplateName, resetPasswordMailSubject);
    }

    private void sendMail(MailRequest mailRequest, Map<String, Object> variables, String templateName, String subject) {
        Mail mail = mailMapper.mapRegisterMailToMail(mailRequest);

        try {
            MimeMessage message = createMimeMessage(mail.getSentToMailAddress(), variables, templateName, subject);
            javaMailSender.send(message);
            mailRepository.save(mail);
            log.info("Email successfully sent to: {}", mail.getSentToMailAddress());
        } catch (MessagingException | IOException | TemplateException | MailSendException e) {
            log.error("Error while sending mail: {}", e.getMessage());
            throw new MailCouldNotSentException(ErrorType.MAIL_COULD_NOT_SENT);
        }
    }

    private MimeMessage createMimeMessage(String recipient, Map<String, Object> variables, String templateName, String subject) throws MessagingException, IOException, TemplateException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(generateMailContent(variables, templateName), true);

        return message;
    }

    private String generateMailContent(Map<String, Object> variables, String templateFileName) throws IOException, TemplateException {
        Template template = freeMarkerConfig.getTemplate(templateFileName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, variables);
    }

    private String createVerificationLink(String verificationToken) {
        return frontendAddress + "/verify?token=" + verificationToken;
    }

    private String createResetPasswordLink(String resetPasswordToken) {
        return frontendAddress + "/reset_password?token=" + resetPasswordToken;
    }
}
