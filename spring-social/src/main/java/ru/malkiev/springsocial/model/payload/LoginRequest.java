package ru.malkiev.springsocial.model.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 5, max = 32)
    private String password;
}
