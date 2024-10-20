package com.dev.identity.service;

import com.dev.identity.entity.EmailTemplate;

public interface EmailTemplateService {

    EmailTemplate getByType(String type);
}
