package ru.malkiev.blog.application.service;

import javax.annotation.PostConstruct;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.malkiev.blog.application.security.CustomUserDetails;
import ru.malkiev.blog.domain.entity.User;

@Component
@SessionScope
public class CurrentUser {

  private boolean authenticated;
  private User user;

  @PostConstruct
  protected void init() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof CustomUserDetails) {
      authenticated = true;
      user = ((CustomUserDetails) principal).getUser();
    }
  }

  public User getUser() {
    if (isAuthenticated()) {
      return user;
    } else {
      throw new IllegalStateException("Current user is not authenticated");
    }
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

}
