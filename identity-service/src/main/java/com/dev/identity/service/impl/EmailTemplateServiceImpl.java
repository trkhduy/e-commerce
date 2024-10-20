package com.dev.identity.service.impl;

import com.dev.identity.entity.EmailTemplate;
import com.dev.identity.repository.EmailTemplateRepository;
import com.dev.identity.service.EmailTemplateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailTemplateServiceImpl implements EmailTemplateService {

    EmailTemplateRepository emailTemplateRepository;

    @Override
    public EmailTemplate getByType(String type) {
        log.info("Getting email template ...");
        return emailTemplateRepository.getEmailTemplateByType(type);
    }
}
