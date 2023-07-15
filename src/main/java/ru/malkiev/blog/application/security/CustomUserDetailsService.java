package ru.malkiev.blog.application.security;


import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.malkiev.blog.application.service.UserService;
import ru.malkiev.blog.domain.entity.User;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) {
    User user = userService.findByEmail(email).orElseThrow(() ->
        new UsernameNotFoundException("User not found with email : " + email)
    );
    return loadUser(user);
  }

  @Transactional
  public UserDetails loadUserById(int id) {
    User user = userService.findById(id).orElseThrow(() ->
        new UsernameNotFoundException("User not found with id : " + id)
    );
    return loadUser(user);
  }

  private CustomUserDetails loadUser(User user) {
    return new CustomUserDetails(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        user,
        Collections.singletonList(user.getRole()),
        null
    );
  }

}