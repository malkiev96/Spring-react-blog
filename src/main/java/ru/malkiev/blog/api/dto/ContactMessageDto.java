package ru.malkiev.blog.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ContactMessageDto {

  @NotEmpty
  private String name;

  @Email
  private String email;

  @NotEmpty
  private String message;

}
