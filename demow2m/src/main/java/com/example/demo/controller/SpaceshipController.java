package com.example.demo.controller;

import com.example.demo.model.SpaceshipDTO;
import com.example.demo.service.SpaceshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/spaceships")
@Tag(name = "Spaceship API", description = "API for managing Movies's spaceships")
public class SpaceshipController {

    @Autowired
    private SpaceshipService spaceshipService;

    @GetMapping
    @Operation(summary = "Get all spaceships", description = "Retrieve a list of all spaceships")
    public Page<SpaceshipDTO> getAllSpaceships(
            @Parameter(description = "Pagination information") @PageableDefault(size = 10) Pageable pageable) {
        return spaceshipService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get spaceship by ID", description = "Retrieve a spaceship by its ID")
    public ResponseEntity<SpaceshipDTO> getSpaceshipById(@PathVariable Long id) {
        return ResponseEntity.ok(spaceshipService.findById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search spaceships by name", description = "Retrieve a list of spaceships whose names contain the specified value")
    public ResponseEntity<List<SpaceshipDTO>> searchSpaceshipsByName(
            @Parameter(description = "Name to search for") @RequestParam String name) {
        List<SpaceshipDTO> spaceships = spaceshipService.findByNameContaining(name);
        return ResponseEntity.ok(spaceships);
    }

    @PostMapping
    @Operation(summary = "Create a new spaceship", description = "Create a new spaceship with the provided details")
    public SpaceshipDTO createSpaceship(@RequestBody SpaceshipDTO spaceshipDTO) {
        return spaceshipService.save(spaceshipDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a spaceship", description = "Update an existing spaceship with the provided details")
    public ResponseEntity<SpaceshipDTO> updateSpaceship(@PathVariable Long id, @RequestBody SpaceshipDTO spaceshipDTODetails) {

        return ResponseEntity.ok(spaceshipService.update(id, spaceshipDTODetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a spaceship", description = "Delete a spaceship by its ID")
    public ResponseEntity<Void> deleteSpaceship(@PathVariable Long id) {
        spaceshipService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}