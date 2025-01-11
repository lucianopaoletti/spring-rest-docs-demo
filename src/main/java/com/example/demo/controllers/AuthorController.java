package com.example.demo.controllers;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.dtos.AuthorDTO;
import com.example.demo.services.AuthorService;
import com.example.demo.validations.groups.Create;
import com.example.demo.validations.groups.Update;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  @PostMapping
  public AuthorDTO create(@RequestBody @Validated(value = Create.class) AuthorDTO author) {
    return this.authorService.create(author);
  }

  @GetMapping
  public List<AuthorDTO> read() {
    return this.authorService.readAll();
  }

  @PutMapping
  public AuthorDTO update(@RequestBody @Validated(value = Update.class) AuthorDTO author) {
    return this.authorService.update(author);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id) {
    this.authorService.delete(id);
  }
}
