package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.models.dtos.AuthorDTO;
import com.example.demo.models.entities.AuthorEntity;
import com.example.demo.models.mappers.AuthorMapper;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.services.AuthorService;
import com.example.demo.validations.exceptions.RequestDataNotFoundException;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTests {

  @Mock
  private AuthorRepository authorRepository;

  @Spy
  private AuthorMapper authorMapper = Mappers.getMapper(AuthorMapper.class);

  @InjectMocks
  private AuthorService authorService;

  @Test
  void create_success() {
    when(this.authorRepository.save(any(AuthorEntity.class))).thenAnswer(invocation -> {
      var author = (AuthorEntity) invocation.getArgument(0);
      return new AuthorEntity(1, author.getName());
    });

    var authorParam = new AuthorDTO(0, "prueba");
    var authorCreated = this.authorService.create(authorParam);
    assertThat(authorCreated).isNotNull();
    assertThat(authorCreated.getId()).isEqualTo(1);
    assertThat(authorCreated.getName()).isEqualTo("prueba");
  }

  @Test
  void read_all_success() {
    when(this.authorRepository.findAll()).thenReturn(Arrays.asList(
        new AuthorEntity(1, "foo"),
        new AuthorEntity(2, "bar")));

    var response = this.authorService.readAll();
    assertThat(response).hasSize(2);
    assertThat(response.get(0).getId()).isEqualTo(1);
  }

  @Test
  void update_success() {
    when(this.authorRepository.findById(any(Integer.class)))
        .thenReturn(Optional.of(new AuthorEntity(1, "foo")));

    when(this.authorRepository.save(any(AuthorEntity.class)))
        .thenReturn(new AuthorEntity(1, "bar"));

    var authorUpdated = this.authorService.update(new AuthorDTO(1, "bar"));
    assertThat(authorUpdated).isNotNull();
    assertThat(authorUpdated.getId()).isEqualTo(1);
    assertThat(authorUpdated.getName()).isEqualTo("bar");
  }

  @Test
  void update_fail_when_not_found() {
    when(this.authorRepository.findById(any(Integer.class)))
        .thenReturn(Optional.empty());

    var author = new AuthorDTO(1, "bar");
    assertThatExceptionOfType(RequestDataNotFoundException.class)
        .isThrownBy(() -> this.authorService.update(author))
        .withMessage("No se encontró el autor con ID 1");
  }

  @Test
  void delete_success() {
    when(this.authorRepository.findById(any(Integer.class)))
        .thenReturn(Optional.of(new AuthorEntity(1, "foo")));

    this.authorService.delete(1);
    verify(this.authorRepository).deleteById(1);
  }

}
