package ru.malkiev.blog.api.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserEditDto {

  @NotEmpty
  private String name;

  private String city;

  private String about;

  private LocalDate birthDate;

  private String previewId;

}
