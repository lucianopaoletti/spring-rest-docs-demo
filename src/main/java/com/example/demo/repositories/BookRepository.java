package com.example.demo.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.example.demo.models.entities.BookEntity;

public interface BookRepository extends ListCrudRepository<BookEntity, Integer> {
}
