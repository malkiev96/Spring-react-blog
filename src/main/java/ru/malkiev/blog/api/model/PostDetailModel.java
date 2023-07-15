package ru.malkiev.blog.api.model;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDetailModel extends PostModel {

  private Integer myStar;
  private Double rating;
  private CategoryModel category;
  private String text;
  private List<TagModel> tags = new LinkedList<>();
  private List<DocumentModel> documents = new LinkedList<>();

}
