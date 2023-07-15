package ru.malkiev.blog.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malkiev.blog.domain.model.DocumentType;

@Data
@NoArgsConstructor
public class DocumentModel {

  private Long id;
  private String filename;
  private DocumentType type;
  private String fileId;
  private Boolean isImage;

  private UserModel createdBy;
  private UserModel lastModifiedBy;
  private String createdDate;
  private String lastModifiedDate;

}
