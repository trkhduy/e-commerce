package com.dev.identity.service.impl;

import com.dev.identity.service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaServiceImpl implements KafkaService {

    KafkaTemplate<String, Object> kafkaTemplate;
    ObjectMapper objectMapper;

    @Override
    public void sendMessage(String topic, String message) {
//        if (object != null) {
//            try {
//                String message = objectMapper.writeValueAsString(object);
//                log.info("Sending..............to {}", topic);
//                kafkaTemplate.send(topic, message);
//            } catch (JsonProcessingException e) {
//                log.error("Error converting object to JSON: {}", e.getMessage());
//            }
//        }
        kafkaTemplate.send(topic, message);
        System.out.println("Message sent to topic: " + topic);
    }
}
