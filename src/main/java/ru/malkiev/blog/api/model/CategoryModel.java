package ru.malkiev.blog.api.model;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryModel {

  private Integer id;
  private String name;
  private String code;
  private List<CategoryModel> children = new LinkedList<>();

}
