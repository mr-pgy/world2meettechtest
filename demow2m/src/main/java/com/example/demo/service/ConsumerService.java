package com.example.demo.service;

import com.example.demo.model.SpaceshipDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @Autowired
    private SpaceshipService spaceshipService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "spaceship_topic", groupId = "group_id")
    public void consume(String message) {
        try {
            SpaceshipDTO spaceshipDTO = objectMapper.readValue(message, SpaceshipDTO.class);
            spaceshipService.save(spaceshipDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
