package com.example.demo.mapper;

import com.example.demo.entity.SpaceshipEntity;
import com.example.demo.model.SpaceshipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SpaceshipMapper {

    SpaceshipMapper INSTANCE = Mappers.getMapper(SpaceshipMapper.class);

    @Mapping(target = "id", source = "id")
    SpaceshipEntity SpaceshipToEntity(SpaceshipDTO source);

    SpaceshipDTO SpaceshipEntityToDTO(SpaceshipEntity source);

}
