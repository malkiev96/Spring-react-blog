package ru.malkiev.blog.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.malkiev.blog.entity.Role;
import ru.malkiev.blog.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CurrentUser implements OAuth2User, UserDetails {

    private Integer id;
    private String email;
    private String password;
    private User user;
    private List<Role> authorities;
    private Map<String, Object> attributes;

    public static CurrentUser create(User user) {
        return new CurrentUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user,
                Collections.singletonList(user.getRole()),
                null
        );
    }

    public static CurrentUser create(User user, Map<String, Object> attributes) {
        CurrentUser currentUser = CurrentUser.create(user);
        currentUser.setAttributes(attributes);
        return currentUser;
    }

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
