package ru.malkiev.blog.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempFileModel {

  private final String fileId;
  private final String filename;

}
