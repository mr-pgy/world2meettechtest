package com.example.demo.service;

import com.example.demo.model.SpaceshipDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private static final String TOPIC = "spaceship_topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(SpaceshipDTO spaceshipDTO) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(spaceshipDTO);
        kafkaTemplate.send(TOPIC, message);
    }
}
