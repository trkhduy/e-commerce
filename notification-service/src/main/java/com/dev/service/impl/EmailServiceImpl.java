package com.dev.service.impl;

import com.dev.enums.EmailPropertiesEnum;
import com.dev.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender javaMailSender;

    @Override
    public void sendEmail(Map<EmailPropertiesEnum, String> valueEmailSendEnumStringMap) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            helper.setTo(valueEmailSendEnumStringMap.get(EmailPropertiesEnum.EMAIL_TO));
            helper.setSubject(valueEmailSendEnumStringMap.get(EmailPropertiesEnum.EMAIL_SUBJECT));
            helper.setText(valueEmailSendEnumStringMap.get(EmailPropertiesEnum.EMAIL_CONTENT), true);

            javaMailSender.send(message);
            log.info("HTML email sent successfully.");

        } catch (MessagingException e) {
            log.error("Catch error when sending mail", e);
        }
    }
}
