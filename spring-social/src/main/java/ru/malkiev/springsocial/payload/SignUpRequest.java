package ru.malkiev.springsocial.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

    @NotNull
    @Size(min = 3, max = 32)
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 5, max = 32)
    private String password;
}
