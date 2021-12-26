package ru.malkiev.blog.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    @NotNull(message = "Email не должен быть пустым")
    @Email
    private String email;

    @NotNull(message = "Пароль не должен быть пустым")
    private String password;
}
