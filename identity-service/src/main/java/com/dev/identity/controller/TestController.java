package com.dev.identity.controller;

import com.dev.identity.service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private KafkaService kafkaProducerService;

    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestBody String emailContent) throws JsonProcessingException {
        String topic = "email-message";
        kafkaProducerService.sendMessage(topic, emailContent);
        return ResponseEntity.ok("Email message sent to Kafka topic!");
    }
}
