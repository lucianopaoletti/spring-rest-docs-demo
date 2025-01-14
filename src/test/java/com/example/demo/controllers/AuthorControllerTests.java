package com.example.demo.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import com.example.demo.models.dtos.AuthorDTO;
import com.example.demo.services.AuthorService;
import java.util.Arrays;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(AuthorController.class)
@AutoConfigureRestDocs
class AuthorControllerTests {

  @Autowired
  private MockMvcTester mvc;

  @MockitoBean
  private AuthorService authorService;

  @Test
  void create_success() {
    when(this.authorService.create(any(AuthorDTO.class)))
        .thenAnswer(invocation -> {
          var author = (AuthorDTO) invocation.getArgument(0);
          return new AuthorDTO(1, author.getName());
        });

    var result = assertThat(
        this.mvc.post()
            .uri("/author")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "prueba"
                }
                    """)
            .accept(MediaType.APPLICATION_JSON))
        .hasStatusOk()
        .hasContentType(MediaType.APPLICATION_JSON);

    result
        .bodyJson()
        .extractingPath("$")
        .convertTo(AuthorDTO.class)
        .satisfies(a -> assertThat(a.getId()).isEqualTo(1))
        .satisfies(a -> assertThat(a.getName()).isEqualTo("prueba"));

    result.apply(
        document(
            "author-create",
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("name").description("Nombre del autor.")),
            responseFields(
                fieldWithPath("id").description("ID generado por el sistema."),
                fieldWithPath("name").description("Nombre del autor ingresado en el request."))));
  }

  @Test
  void read_success() {
    when(this.authorService.readAll()).thenReturn(
        Arrays.asList(new AuthorDTO(1, "foo"), new AuthorDTO(2, "bar")));

    var result = assertThat(
        this.mvc.get()
            .uri("/author")
            .accept(MediaType.APPLICATION_JSON))
        .hasStatusOk()
        .hasContentType(MediaType.APPLICATION_JSON);

    result
        .bodyJson()
        .extractingPath("$")
        .convertTo(InstanceOfAssertFactories.list(AuthorDTO.class))
        .hasSize(2)
        .element(1)
        .satisfies(a -> assertThat(a.getId()).isEqualTo(2))
        .satisfies(a -> assertThat(a.getName()).isEqualTo("bar"));

    result.apply(
        document("author-read", preprocessResponse(prettyPrint())));
  }

  @Test
  void update_success() {
    var authorBeforeUpdate = new AuthorDTO(1, "foo");
    var authorAfterUpdate = new AuthorDTO(1, "bar");

    when(this.authorService.update(any(AuthorDTO.class)))
        .thenReturn(authorAfterUpdate);

    var result = assertThat(
        this.mvc.put()
            .uri("/author")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 1,
                  "name": "bar"
                }
                    """)
            .accept(MediaType.APPLICATION_JSON))
        .hasStatusOk()
        .hasContentType(MediaType.APPLICATION_JSON);

    result
        .bodyJson()
        .extractingPath("$")
        .convertTo(AuthorDTO.class)
        .isNotNull()
        .satisfies(a -> assertThat(a.getId()).isEqualTo(authorBeforeUpdate.getId()))
        .satisfies(a -> assertThat(a.getName()).isEqualTo(authorAfterUpdate.getName()));

    result.apply(
        document("author-update",
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("id").description("ID existente del autor."),
                fieldWithPath("name").description("Nombre del autor a actualizar.")),
            responseFields(
                fieldWithPath("id").description("ID del autor."),
                fieldWithPath("name").description("Nombre actualizado del autor."))));
  }

  @Test
  void delete_success() {
    var result = assertThat(this.mvc.delete().uri("/author/{id}", 1))
        .hasStatusOk();

    result.body()
        .isEmpty();

    result.apply(
        document("author-delete", pathParameters(
            parameterWithName("id").description("ID del autor a eliminar."))));
  }
}
