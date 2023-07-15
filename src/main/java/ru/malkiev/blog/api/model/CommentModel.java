package ru.malkiev.blog.api.model;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentModel {

  private Integer id;
  private String message;
  private boolean deleted;
  private List<CommentModel> children = new LinkedList<>();

  private UserModel createdBy;
  private UserModel lastModifiedBy;
  private String createdDate;
  private String lastModifiedDate;

}
