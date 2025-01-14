package com.example.demo.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import java.util.Arrays;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.example.demo.models.dtos.BookDTO;
import com.example.demo.services.BookService;

@WebMvcTest(BookController.class)
@AutoConfigureRestDocs
class BookControllerTests {

  @Autowired
  private MockMvcTester mvc;

  @MockitoBean
  private BookService bookService;

  @Test
  void book_create_success() {
    when(this.bookService.create(any(BookDTO.class)))
        .thenAnswer(invocation -> {
          var book = (BookDTO) invocation.getArgument(0);
          return new BookDTO(1, book.getName(), book.getAuthorId());
        });

    var result = assertThat(
        this.mvc.post()
            .uri("/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "prueba",
                  "authorId": 1
                }
                    """)
            .accept(MediaType.APPLICATION_JSON))
        .hasStatusOk()
        .hasContentType(MediaType.APPLICATION_JSON);

    result
        .bodyJson()
        .extractingPath("$")
        .convertTo(BookDTO.class)
        .satisfies(a -> assertThat(a.getId()).isEqualTo(1))
        .satisfies(a -> assertThat(a.getName()).isEqualTo("prueba"))
        .satisfies(a -> assertThat(a.getAuthorId()).isEqualTo(1));

    result.apply(
        document("book-create",
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("name").description("Nombre del libro."),
                fieldWithPath("authorId").description("ID del autor.")),
            responseFields(
                fieldWithPath("id").description("ID generado por el sistema."),
                fieldWithPath("name").description("Nombre del libro ingresado en el request"),
                fieldWithPath("authorId").description("ID del autor ingresado en el request"))));
  }

  @Test
  void book_read_success() {
    when(this.bookService.readAll()).thenReturn(
        Arrays.asList(new BookDTO(1, "foo", 5), new BookDTO(2, "bar", 3)));

    var result = assertThat(
        this.mvc.get()
            .uri("/book")
            .accept(MediaType.APPLICATION_JSON))
        .hasStatusOk()
        .hasContentType(MediaType.APPLICATION_JSON);

    result
        .bodyJson()
        .extractingPath("$")
        .convertTo(InstanceOfAssertFactories.list(BookDTO.class))
        .hasSize(2)
        .element(1)
        .satisfies(a -> assertThat(a.getId()).isEqualTo(2))
        .satisfies(a -> assertThat(a.getName()).isEqualTo("bar"))
        .satisfies(a -> assertThat(a.getAuthorId()).isEqualTo(3));

    result.apply(
        document("book-read", preprocessResponse(prettyPrint())));
  }

  @Test
  void book_update_success() {
    var bookBeforeUpdate = new BookDTO(1, "lorem", 14);
    var bookAfterUpdate = new BookDTO(1, "ipsum", 14);

    when(this.bookService.update(any(BookDTO.class)))
        .thenReturn(bookAfterUpdate);

    var result = assertThat(
        this.mvc.put()
            .uri("/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 1,
                  "name": "ipsum",
                  "authorId": 14
                }
                    """)
            .accept(MediaType.APPLICATION_JSON))
        .hasStatusOk()
        .hasContentType(MediaType.APPLICATION_JSON);

    result
        .bodyJson()
        .extractingPath("$")
        .convertTo(BookDTO.class)
        .isNotNull()
        .satisfies(a -> assertThat(a.getId()).isEqualTo(bookBeforeUpdate.getId()))
        .satisfies(a -> assertThat(a.getName()).isEqualTo(bookAfterUpdate.getName()))
        .satisfies(a -> assertThat(a.getAuthorId()).isEqualTo(bookAfterUpdate.getAuthorId()));

    result.apply(
        document("book-update",
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("id").description("ID existente del libro."),
                fieldWithPath("name").description("Nombre del libro a actualizar."),
                fieldWithPath("authorId").description("ID del autor del libro a actualizar.")),
            responseFields(
                fieldWithPath("id").description("ID del libro."),
                fieldWithPath("name").description("Nombre actualizado del libro."),
                fieldWithPath("authorId").description("ID del nuevo autor."))));
  }

  @Test
  void book_delete_success() {
    var result = assertThat(
        this.mvc.delete()
            .uri("/book/{id}", 1))
        .hasStatusOk();

    result.body().isEmpty();

    result.apply(
        document("book-delete", pathParameters(
            parameterWithName("id").description("ID del libro a eliminar."))));
  }
}
