package ru.malkiev.blog.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryParentModel {

  private Integer id;
  private String name;
  private String code;
  private CategoryParentModel parent;

}
