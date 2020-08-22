package ru.malkiev.springsocial.model.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @NotNull(message = "Email не должен быть пустым")
    @Email
    private String email;

    @NotNull(message = "Пароль не должен быть пустым")
    private String password;
}
