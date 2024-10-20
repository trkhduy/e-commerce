package com.dev.identity.util;

import com.dev.enums.EmailPropertiesEnum;
import com.dev.enums.EmailTypeEnum;
import com.dev.enums.TokenTypeEnum;
import com.dev.identity.entity.EmailTemplate;
import com.dev.identity.entity.User;
import com.dev.identity.service.AuthenticationService;
import com.dev.identity.service.EmailTemplateService;
import com.dev.identity.service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SendEmailUtil {

    KafkaService kafkaService;
    AuthenticationService authenticationService;
    EmailTemplateService emailTemplateService;

    @Value("${path.verifyEmail}")
    @NonFinal
    String pathVerifyEmail;
    @Value("${path.forgotPassword}")
    @NonFinal
    String pathForgotPassword;

    @Value("${spring.kafka.topic.send.email}")
    @NonFinal
    String topicMessageEmail;


    public void sendVerifyEmail(User user) throws JsonProcessingException {
        Map<EmailPropertiesEnum, String> valueEmailSendEnumStringMap = handleInformationEmail(user, EmailTypeEnum.VERIFY_EMAIL);
        kafkaService.sendMessage(topicMessageEmail, valueEmailSendEnumStringMap);
    }

    private Map<EmailPropertiesEnum, String> handleInformationEmail(User user, EmailTypeEnum emailTypeEnum) {
        String email = user.getEmail();
        TokenTypeEnum typeToken;
        switch (emailTypeEnum) {
            case VERIFY_EMAIL -> typeToken = TokenTypeEnum.VERIFY_TOKEN;
            case FORGOT_PASSWORD -> typeToken = TokenTypeEnum.FORGOT_PASSWORD_TOKEN;
            default -> typeToken = null;
        }
        String linkVerifyToken = typeToken.equals(TokenTypeEnum.VERIFY_TOKEN)
                ? pathVerifyEmail
                : pathForgotPassword;
        linkVerifyToken = linkVerifyToken.replace("{{userId}}", user.getId());
        linkVerifyToken = linkVerifyToken.replace("{{token}}", authenticationService.generateVerifyMailToken(user));
        EmailTemplate emailTemplateEntity = emailTemplateService.getByType(emailTypeEnum.toString());
        String subject = emailTemplateEntity.getSubject();
        String contentEmail = emailTemplateEntity.getContent();
        String fullName = user.getFirstName() + " " + user.getLastName();
        contentEmail = contentEmail.replace("{{User}}", fullName);
        contentEmail = contentEmail.replace("{{Link}}", linkVerifyToken);
        Map<EmailPropertiesEnum, String> keyEmailMap = new HashMap<>();
        keyEmailMap.put(EmailPropertiesEnum.EMAIL_TO, email);
        keyEmailMap.put(EmailPropertiesEnum.EMAIL_CONTENT, contentEmail);
        keyEmailMap.put(EmailPropertiesEnum.EMAIL_SUBJECT, subject);
        return keyEmailMap;
    }
}
