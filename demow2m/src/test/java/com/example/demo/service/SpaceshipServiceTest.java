package com.example.demo.service;

import com.example.demo.entity.SpaceshipEntity;
import com.example.demo.exception.IdAlreadyExistsException;
import com.example.demo.exception.SpaceshipNotFoundException;
import com.example.demo.mapper.SpaceshipMapper;
import com.example.demo.model.SpaceshipDTO;
import com.example.demo.repository.SpaceshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpaceshipServiceTest {

    @Mock
    private SpaceshipRepository spaceshipRepository;

    @Mock
    private SpaceshipMapper spaceshipMapper;

    @InjectMocks
    private SpaceshipService spaceshipService;

    private SpaceshipEntity spaceshipEntity;
    private SpaceshipDTO spaceshipDTO;

    @BeforeEach
    void setUp() {
        spaceshipEntity = new SpaceshipEntity();
        spaceshipEntity.setId(1L);
        spaceshipEntity.setName("X-wing");

        spaceshipDTO = new SpaceshipDTO();
        spaceshipDTO.setId(1L);
        spaceshipDTO.setName("X-wing");
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SpaceshipEntity> spaceshipPage = new PageImpl<>(Collections.singletonList(spaceshipEntity));
        when(spaceshipRepository.findAll(pageable)).thenReturn(spaceshipPage);
        when(spaceshipMapper.SpaceshipEntityToDTO(any(SpaceshipEntity.class))).thenReturn(spaceshipDTO);

        Page<SpaceshipDTO> result = spaceshipService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(spaceshipRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById() {
        when(spaceshipRepository.findById(1L)).thenReturn(Optional.of(spaceshipEntity));
        when(spaceshipMapper.SpaceshipEntityToDTO(any(SpaceshipEntity.class))).thenReturn(spaceshipDTO);

        SpaceshipDTO result = spaceshipService.findById(1L);

        assertNotNull(result);
        assertEquals("X-wing", result.getName());
        verify(spaceshipRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(spaceshipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SpaceshipNotFoundException.class, () -> spaceshipService.findById(1L));
        verify(spaceshipRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByNameContaining() {
        when(spaceshipRepository.findByNameContainingIgnoreCase("wing")).thenReturn(Collections.singletonList(spaceshipEntity));
        when(spaceshipMapper.SpaceshipEntityToDTO(any(SpaceshipEntity.class))).thenReturn(spaceshipDTO);

        List<SpaceshipDTO> result = spaceshipService.findByNameContaining("wing");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(spaceshipRepository, times(1)).findByNameContainingIgnoreCase("wing");
    }

    @Test
    void testSave() {
        when(spaceshipRepository.save(any(SpaceshipEntity.class))).thenReturn(spaceshipEntity);
        when(spaceshipMapper.SpaceshipToEntity(any(SpaceshipDTO.class))).thenReturn(spaceshipEntity);
        when(spaceshipMapper.SpaceshipEntityToDTO(any(SpaceshipEntity.class))).thenReturn(spaceshipDTO);

        SpaceshipDTO result = spaceshipService.save(spaceshipDTO);

        assertNotNull(result);
        assertEquals("X-wing", result.getName());
        verify(spaceshipRepository, times(1)).save(any(SpaceshipEntity.class));
    }

    @Test
    void testSaveIdAlreadyExists() {
        spaceshipDTO.setId(1L);
        when(spaceshipRepository.findById(1L)).thenReturn(Optional.of(spaceshipEntity));

        assertThrows(IdAlreadyExistsException.class, () -> spaceshipService.save(spaceshipDTO));
        verify(spaceshipRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdate() {
        when(spaceshipRepository.findById(1L)).thenReturn(Optional.empty());
        when(spaceshipRepository.save(any(SpaceshipEntity.class))).thenReturn(spaceshipEntity);
        when(spaceshipMapper.SpaceshipToEntity(any(SpaceshipDTO.class))).thenReturn(spaceshipEntity);
        when(spaceshipMapper.SpaceshipEntityToDTO(any(SpaceshipEntity.class))).thenReturn(spaceshipDTO);

        SpaceshipDTO result = spaceshipService.update(1L, spaceshipDTO);

        assertNotNull(result);
        assertEquals("X-wing", result.getName());
        verify(spaceshipRepository, times(1)).save(any(SpaceshipEntity.class));
    }

    @Test
    void testUpdateIdNotFound() {
        when(spaceshipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SpaceshipNotFoundException.class, () -> spaceshipService.update(1L, spaceshipDTO));
    }

    @Test
    void testUpdateIdAlreadyExists() {
        when(spaceshipRepository.findById(1L)).thenReturn(Optional.of(spaceshipEntity));

        assertThrows(IdAlreadyExistsException.class, () -> spaceshipService.update(1L, spaceshipDTO));
        verify(spaceshipRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        spaceshipService.deleteById(1L);
        verify(spaceshipRepository, times(1)).deleteById(1L);
    }
}
