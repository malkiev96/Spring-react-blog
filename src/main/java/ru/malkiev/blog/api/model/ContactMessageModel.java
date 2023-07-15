package ru.malkiev.blog.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactMessageModel {

  private Integer id;
  private String name;
  private String email;
  private String message;

  private String createdDate;

}
