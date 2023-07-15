package ru.malkiev.blog.api.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malkiev.blog.domain.model.Role;

@Data
@NoArgsConstructor
public class UserDetailModel {

  private Integer id;
  private String name;
  private String email;
  private Boolean emailVerified;
  private String city;
  private String about;
  private LocalDate birthDate;
  private Role role;
  private TempFileModel preview;

}
