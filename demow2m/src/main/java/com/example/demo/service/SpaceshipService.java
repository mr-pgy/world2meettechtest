package com.example.demo.service;

import com.example.demo.entity.SpaceshipEntity;
import com.example.demo.exception.IdAlreadyExistsException;
import com.example.demo.exception.SpaceshipNotFoundException;
import com.example.demo.mapper.SpaceshipMapper;
import com.example.demo.model.SpaceshipDTO;
import com.example.demo.repository.SpaceshipRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.constant.SpaceshipsConstants.SPACESHIPS;
import static com.example.demo.constant.SpaceshipsConstants.SPACESHIPS_BY_NAME;

@Service
@AllArgsConstructor
public class SpaceshipService {

    @Autowired
    private SpaceshipRepository spaceshipRepository;
    @Autowired
    private SpaceshipMapper spaceshipMapper;

    @Cacheable(SPACESHIPS)
    public Page<SpaceshipDTO> findAll(Pageable pageable) {
        Page<SpaceshipEntity> spaceshipPage = spaceshipRepository.findAll(pageable);
        return spaceshipPage.map(spaceshipMapper::SpaceshipEntityToDTO);
    }

    public SpaceshipDTO findById(Long id) {
        return spaceshipRepository.findById(id).map(spaceshipMapper::SpaceshipEntityToDTO)
                .orElseThrow(() -> new SpaceshipNotFoundException("Spaceship not found with id: " + id));
    }

    @Cacheable(value = SPACESHIPS_BY_NAME, key = "#name")
    public List<SpaceshipDTO> findByNameContaining(String name) {
        return spaceshipRepository.findByNameContainingIgnoreCase(name).stream()
                .map(spaceshipMapper::SpaceshipEntityToDTO)
                .collect(Collectors.toList());
    }

    @Caching(
            evict = {
                    @CacheEvict(value = SPACESHIPS, allEntries = true),
                    @CacheEvict(value = SPACESHIPS_BY_NAME, allEntries = true)
            }
    )
    public SpaceshipDTO save(SpaceshipDTO spaceshipDTO) {
        if (Objects.nonNull(spaceshipDTO.getId())) {
            spaceshipRepository.findById(spaceshipDTO.getId()).ifPresent(s -> {
                throw new IdAlreadyExistsException("this id already exists");
            });
        }
        SpaceshipEntity entity = spaceshipMapper.SpaceshipToEntity(spaceshipDTO);
        return spaceshipMapper.SpaceshipEntityToDTO(spaceshipRepository.save(entity));
    }

    @Caching(
            evict = {
                    @CacheEvict(value = SPACESHIPS, allEntries = true),
                    @CacheEvict(value = SPACESHIPS_BY_NAME, allEntries = true)
            }
    )
    public SpaceshipDTO update(Long id, SpaceshipDTO spaceshipDTO) {

        return spaceshipRepository.findById(id).map(spaceshipEntity -> {
            spaceshipEntity.setName(spaceshipDTO.getName());
            return spaceshipMapper.SpaceshipEntityToDTO(spaceshipRepository.save(spaceshipEntity));
        }).orElseThrow(() -> new SpaceshipNotFoundException("id not found"));
    }

    @Caching(
            evict = {
                    @CacheEvict(value = SPACESHIPS, allEntries = true),
                    @CacheEvict(value = SPACESHIPS_BY_NAME, allEntries = true)
            }
    )
    public void deleteById(Long id) {
        spaceshipRepository.deleteById(id);
    }

}

