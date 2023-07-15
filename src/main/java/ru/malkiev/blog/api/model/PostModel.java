package ru.malkiev.blog.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malkiev.blog.domain.model.PostStatus;

@Data
@NoArgsConstructor
public class PostModel {

  private Integer id;
  private String title;
  private String description;
  private int viewCount;
  private PostStatus status;
  private DocumentModel preview;

  private UserModel createdBy;
  private UserModel lastModifiedBy;
  private String createdDate;
  private String lastModifiedDate;

}
