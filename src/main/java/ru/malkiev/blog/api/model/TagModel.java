package ru.malkiev.blog.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagModel {

  private Integer id;
  private String name;
  private String code;

}
