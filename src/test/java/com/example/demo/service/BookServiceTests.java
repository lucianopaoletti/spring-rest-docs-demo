package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

import com.example.demo.models.dtos.BookDTO;
import com.example.demo.models.entities.AuthorEntity;
import com.example.demo.models.entities.BookEntity;
import com.example.demo.models.mappers.BookMapper;
import com.example.demo.repositories.BookRepository;
import com.example.demo.services.AuthorService;
import com.example.demo.services.BookService;
import com.example.demo.validations.exceptions.RequestDataNotFoundException;

@ExtendWith(MockitoExtension.class)
class BookServiceTests {

  @Mock
  private BookRepository bookRepository;

  @Spy
  private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

  @Mock
  private AuthorService authorService;

  @InjectMocks
  private BookService bookService;

  @Test
  void create_success() {
    when(this.bookRepository.save(any(BookEntity.class))).thenAnswer(invocation -> {
      var book = (BookEntity) invocation.getArgument(0);
      return new BookEntity(1, book.getName(), book.getAuthor());
    });

    var bookParam = new BookDTO(0, "prueba", 5);
    var bookCreated = this.bookService.create(bookParam);
    assertThat(bookCreated).isNotNull();
    assertThat(bookCreated.getId()).isEqualTo(1);
    assertThat(bookCreated.getName()).isEqualTo("prueba");
    assertThat(bookCreated.getAuthorId()).isEqualTo(5);
  }

  @Test
  void read_all_success() {
    when(this.bookRepository.findAll()).thenReturn(Arrays.asList(
        new BookEntity(1, "lorem ipsum", new AuthorEntity(3, "foo")),
        new BookEntity(2, "ipsum lorem", new AuthorEntity(18, "bar"))));

    var response = this.bookService.readAll();
    assertThat(response).hasSize(2);
    assertThat(response.get(0).getId()).isEqualTo(1);
    assertThat(response.get(0).getAuthorId()).isEqualTo(3);
  }

  @Test
  void update_success() {
    when(this.bookRepository.findById(any(Integer.class)))
        .thenReturn(Optional.of(new BookEntity(1, "lorem ipsum", new AuthorEntity(2, "foo"))));

    when(this.bookRepository.save(any(BookEntity.class)))
        .thenReturn(new BookEntity(1, "lorem ipsum", new AuthorEntity(3, "bar")));

    var bookUpdated = this.bookService.update(new BookDTO(1, "lorem ipsum", 3));
    assertThat(bookUpdated).isNotNull();
    assertThat(bookUpdated.getId()).isEqualTo(1);
    assertThat(bookUpdated.getName()).isEqualTo("lorem ipsum");
    assertThat(bookUpdated.getAuthorId()).isEqualTo(3);
  }

  @Test
  void update_fail_when_not_found() {
    when(this.bookRepository.findById(any(Integer.class)))
        .thenReturn(Optional.empty());

    var book = new BookDTO(3, "foo", 4);
    assertThatExceptionOfType(RequestDataNotFoundException.class)
        .isThrownBy(() -> this.bookService.update(book))
        .withMessage("No se encontró el libro con ID 3");
  }

  @Test
  void delete_success() {
    when(this.bookRepository.findById(any(Integer.class)))
        .thenReturn(Optional.of(new BookEntity(2, "lorem", new AuthorEntity(1, "foo"))));

    this.bookService.delete(2);
    verify(this.bookRepository).deleteById(2);
  }
}
