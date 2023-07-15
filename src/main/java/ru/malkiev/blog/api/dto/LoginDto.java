package ru.malkiev.blog.api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

  @NotNull(message = "Email не должен быть пустым")
  @Email
  private String email;

  @NotNull(message = "Пароль не должен быть пустым")
  private String password;
}
