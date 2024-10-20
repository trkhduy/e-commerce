package com.dev.identity.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface KafkaService {
    void sendMessage(String topic, Object object) throws JsonProcessingException;
}
