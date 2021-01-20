package ru.malkiev.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class UserEditDto {

    @NotEmpty
    private String name;

    private String imageUrl;

    private String city;

    private String about;

    private LocalDate birthDate;
}
