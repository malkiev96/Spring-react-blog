package ru.malkiev.springsocial.model.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

    @NotNull(message = "Имя не должно быть пустым")
    @Size(min = 3, max = 32, message = "Длина имени должна быть от 3 до 32 символов")
    private String name;

    @NotNull(message = "Email не должен быть пустым")
    @Email
    private String email;

    @NotNull(message = "Пароль не должен быть пустым")
    @Size(min = 5, max = 32, message = "Длина пароля должна быть от 5 до 32 символов")
    private String password;
}
