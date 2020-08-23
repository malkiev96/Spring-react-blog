package ru.malkiev.blog.security;


import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.service.UserService;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email : " + email)
        );
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(int id) throws UsernameNotFoundException {
        User user = userService.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User not found with id : " + id)
        );
        return UserPrincipal.create(user);
    }
}