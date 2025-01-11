package com.example.demo.models.mappers;

import org.mapstruct.Mapper;

import com.example.demo.models.dtos.AuthorDTO;
import com.example.demo.models.entities.AuthorEntity;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
  AuthorDTO toDTO(AuthorEntity entity);

  AuthorEntity toEntity(AuthorDTO dto);
}
