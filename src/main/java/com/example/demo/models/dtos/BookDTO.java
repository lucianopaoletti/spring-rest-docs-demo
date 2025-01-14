package com.example.demo.models.dtos;

import com.example.demo.validations.groups.Create;
import com.example.demo.validations.groups.Update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
  @NotNull(groups = Update.class)
  Integer id;

  @NotBlank(groups = { Create.class, Update.class })
  String name;

  @NotNull(groups = { Create.class, Update.class })
  @Min(value = 0, groups = { Create.class, Update.class })
  Integer authorId;
}
