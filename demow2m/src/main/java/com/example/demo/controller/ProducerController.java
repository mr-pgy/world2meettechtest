package com.example.demo.controller;

import com.example.demo.model.SpaceshipDTO;
import com.example.demo.service.ProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/producer")
@Tag(name = "Spaceship API", description = "API for managing Movies's spaceships")
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @PostMapping
    @Operation(summary = "Create a new spaceship", description = "Create a new message to create a new spaceship with the provided")
    public ResponseEntity<String> createSpaceshipMessage(@RequestBody SpaceshipDTO spaceshipDTO) throws JsonProcessingException {
        producerService.sendMessage(spaceshipDTO);
        return ResponseEntity.ok(" Spaceship now in queue ");
    }
}