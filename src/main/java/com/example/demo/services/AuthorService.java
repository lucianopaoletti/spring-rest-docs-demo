package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.models.dtos.AuthorDTO;
import com.example.demo.models.mappers.AuthorMapper;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.validations.exceptions.RequestDataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {
  private final AuthorRepository authorRepository;
  private final AuthorMapper authorMapper;

  public AuthorDTO create(AuthorDTO authorParam) {
    var entity = this.authorMapper.toEntity(authorParam);
    entity.setId(null);
    var entitySaved = this.authorRepository.save(entity);
    return this.authorMapper.toDTO(entitySaved);
  }

  public List<AuthorDTO> readAll() {
    return this.authorRepository.findAll()
        .stream()
        .map(this.authorMapper::toDTO)
        .toList();
  }

  public AuthorDTO update(AuthorDTO authorParam) {
    this.validateExistence(authorParam.getId());
    var entity = this.authorMapper.toEntity(authorParam);
    var entitySaved = this.authorRepository.save(entity);
    return this.authorMapper.toDTO(entitySaved);
  }

  public void delete(Integer id) {
    this.validateExistence(id);
    this.authorRepository.deleteById(id);
  }

  public void validateExistence(Integer id) {
    var exists = this.authorRepository.findById(id).isPresent();
    if (!exists) {
      throw new RequestDataNotFoundException(
          String.format("No se encontró el autor con ID %d", id));
    }
  }
}
