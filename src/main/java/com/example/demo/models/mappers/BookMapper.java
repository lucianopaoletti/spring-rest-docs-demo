package com.example.demo.models.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.models.dtos.BookDTO;
import com.example.demo.models.entities.BookEntity;

@Mapper(componentModel = "spring")
public interface BookMapper {
  @Mapping(target = "authorId", source = "author.id")
  BookDTO toDTO(BookEntity entity);

  @Mapping(target = "author.id", source = "authorId")
  BookEntity toEntity(BookDTO dto);
}
