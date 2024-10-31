package com.example.demo.integration.test;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.entity.SpaceshipEntity;
import com.example.demo.model.SpaceshipDTO;
import com.example.demo.repository.SpaceshipRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpaceshipIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    @BeforeEach
    public void setUp() {
        spaceshipRepository.deleteAll();
    }

    @Test
    public void testCreateSpaceship() throws Exception {
        SpaceshipDTO spaceship = new SpaceshipDTO();
        spaceship.setName("Enterprise");

        mockMvc.perform(post("/api/spaceships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(spaceship)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Enterprise"));
    }

    @Test
    public void testGetSpaceshipById() throws Exception {
        SpaceshipEntity spaceship = new SpaceshipEntity();
        spaceship.setName("Millennium Falcon");
        spaceship = spaceshipRepository.save(spaceship);

        mockMvc.perform(get("/api/spaceships/" + spaceship.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Millennium Falcon"));
    }

    @Test
    public void testGetAllSpaceships() throws Exception {
        SpaceshipEntity spaceship1 = new SpaceshipEntity();
        spaceship1.setName("X-Wing");
        spaceshipRepository.save(spaceship1);

        SpaceshipEntity spaceship2 = new SpaceshipEntity();
        spaceship2.setName("Serenity");
        spaceshipRepository.save(spaceship2);

        mockMvc.perform(get("/api/spaceships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("X-Wing"))
                .andExpect(jsonPath("$.content[1].name").value("Serenity"));
    }

    @Test
    public void testUpdateSpaceship() throws Exception {
        SpaceshipEntity spaceship = new SpaceshipEntity();
        spaceship.setName("Discovery");
        spaceship = spaceshipRepository.save(spaceship);

        spaceship.setName("USS Discovery");

        mockMvc.perform(put("/api/spaceships/" + spaceship.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(spaceship)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("USS Discovery"));
    }

    @Test
    public void testDeleteSpaceship() throws Exception {
        SpaceshipEntity spaceship = new SpaceshipEntity();
        spaceship.setName("Galactica");
        spaceship = spaceshipRepository.save(spaceship);

        mockMvc.perform(delete("/api/spaceships/" + spaceship.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(spaceshipRepository.findById(spaceship.getId()).isPresent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
