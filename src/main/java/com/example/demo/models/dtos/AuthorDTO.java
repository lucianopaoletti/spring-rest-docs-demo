package com.example.demo.models.dtos;

import com.example.demo.validations.groups.Create;
import com.example.demo.validations.groups.Update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {
  @NotNull(groups = Update.class)
  Integer id;

  @NotBlank(groups = Create.class)
  String name;
}
