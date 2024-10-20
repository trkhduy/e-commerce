package com.dev.identity.repository;

import com.dev.identity.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, String> {

    EmailTemplate getEmailTemplateByType(String type);
}
