package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.models.dtos.BookDTO;
import com.example.demo.models.mappers.BookMapper;
import com.example.demo.repositories.BookRepository;
import com.example.demo.validations.exceptions.RequestDataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

  private final AuthorService authorService;

  public BookDTO create(BookDTO bookParam) {
    this.authorService.validateExistence(bookParam.getAuthorId());
    var entity = this.bookMapper.toEntity(bookParam);
    var entitySaved = this.bookRepository.save(entity);
    return this.bookMapper.toDTO(entitySaved);
  }

  public List<BookDTO> readAll() {
    return this.bookRepository.findAll()
        .stream()
        .map(this.bookMapper::toDTO)
        .toList();
  }

  public BookDTO update(BookDTO bookParam) {
    this.validateExistence(bookParam.getId());
    this.authorService.validateExistence(bookParam.getAuthorId());
    var entity = this.bookMapper.toEntity(bookParam);
    var entitySaved = this.bookRepository.save(entity);
    return this.bookMapper.toDTO(entitySaved);
  }

  public void delete(Integer id) {
    this.validateExistence(id);
    this.bookRepository.deleteById(id);
  }

  public void validateExistence(Integer id) {
    var exists = this.bookRepository.findById(id).isPresent();
    if (!exists) {
      throw new RequestDataNotFoundException(String.format("No se encontró el libro con ID %d", id));
    }
  }
}
