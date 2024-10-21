package com.dev.listeners;

import com.dev.enums.EmailPropertiesEnum;
import com.dev.service.EmailService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaListenerService {
    @Value("${spring.kafka.topic.send.email}")
    @NonFinal
    String topicMessageEmail;

    EmailService emailService;

    @KafkaListener(topics = "${spring.kafka.topic.send.email}", containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessageSendMessage(String message) {
        log.info("Receiving message... {}", message);
        try {
            Map<EmailPropertiesEnum, String> valueEmailSendEnumStringMap = convertMessageToMap(message);
            emailService.sendEmail(valueEmailSendEnumStringMap);
        } catch (Exception e) {
            log.error("Error processing Kafka message", e);
        }
    }

    private Map<EmailPropertiesEnum, String> convertMessageToMap(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(message, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Error processing Kafka message: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

}
