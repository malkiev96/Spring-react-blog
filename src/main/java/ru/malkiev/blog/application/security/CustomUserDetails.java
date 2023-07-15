package ru.malkiev.blog.application.security;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.malkiev.blog.domain.entity.User;
import ru.malkiev.blog.domain.model.Role;

@Data
@AllArgsConstructor
public class CustomUserDetails implements OAuth2User, UserDetails {

  private Integer id;
  private String email;
  private String password;
  private User user;
  private List<Role> authorities;
  private Map<String, Object> attributes;

  @Override
  public String getName() {
    return String.valueOf(id);
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
