package ru.malkiev.blog.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malkiev.blog.domain.model.Role;

@Data
@NoArgsConstructor
public class UserModel {

  private Integer id;
  private String name;
  private String email;
  private Role role;
  private TempFileModel preview;

}
