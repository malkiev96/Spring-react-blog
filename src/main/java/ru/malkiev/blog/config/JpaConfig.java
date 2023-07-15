package ru.malkiev.blog.config;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.malkiev.blog.application.security.CustomUserDetails;
import ru.malkiev.blog.domain.entity.User;

@Configuration
@EnableJpaAuditing
@AllArgsConstructor
public class JpaConfig {

  @Bean
  public AuditorAware<User> auditorProvider() {
    return () -> {
      var authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication.getPrincipal() instanceof CustomUserDetails) {
        var userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        return Optional.of(userPrincipal.getUser());
      }
      return Optional.empty();
    };
  }
}
