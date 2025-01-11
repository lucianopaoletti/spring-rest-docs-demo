package com.example.demo.repositories;

import org.springframework.data.repository.ListCrudRepository;

import com.example.demo.models.entities.AuthorEntity;

public interface AuthorRepository extends ListCrudRepository<AuthorEntity, Integer> {
}
