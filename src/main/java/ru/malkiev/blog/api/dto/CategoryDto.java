package ru.malkiev.blog.api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {

  private Integer categoryId;
  private Integer parentId;

  @NotNull
  @Size(min = 3, max = 16)
  private String code;

  @NotNull
  @Size(min = 3, max = 32)
  private String name;
}
