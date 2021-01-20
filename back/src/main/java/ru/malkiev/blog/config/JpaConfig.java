package ru.malkiev.blog.config;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.repository.UserRepository;
import ru.malkiev.blog.security.UserPrincipal;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@AllArgsConstructor
public class JpaConfig {

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<User> {

        @Autowired
        private UserRepository userRepository;

        @Override
        public @NotNull Optional<User> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }
            if (authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                return userRepository.findById(userPrincipal.getId());
            }
            return Optional.empty();
        }
    }
}
