package com.dev.service;

import com.dev.enums.EmailPropertiesEnum;

import java.util.Map;

public interface EmailService {
    void sendEmail(Map<EmailPropertiesEnum, String> valueEmailSendEnumStringMap);
}
