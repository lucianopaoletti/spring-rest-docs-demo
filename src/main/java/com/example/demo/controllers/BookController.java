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

import com.example.demo.models.dtos.BookDTO;
import com.example.demo.services.BookService;
import com.example.demo.validations.groups.Create;
import com.example.demo.validations.groups.Update;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @PostMapping
  public BookDTO create(@RequestBody @Validated(value = Create.class) BookDTO bookParam) {
    return this.bookService.create(bookParam);
  }

  @GetMapping
  public List<BookDTO> readAll() {
    return this.bookService.readAll();
  }

  @PutMapping
  public BookDTO update(@RequestBody @Validated(value = Update.class) BookDTO bookParam) {
    return this.bookService.update(bookParam);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id) {
    this.bookService.delete(id);
  }
}
