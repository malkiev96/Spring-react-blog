package ru.malkiev.blog.service;

import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.malkiev.blog.dto.SignUpDto;
import ru.malkiev.blog.entity.AuthProvider;
import ru.malkiev.blog.entity.Role;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.exception.BadRequestException;
import ru.malkiev.blog.exception.UserNotFoundException;
import ru.malkiev.blog.repository.UserRepository;
import ru.malkiev.blog.security.CurrentUser;
import ru.malkiev.blog.security.oauth2.user.OAuth2UserInfo;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> getCurrentUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CurrentUser) {
            return Optional.of(((CurrentUser) principal).getUser());
        }
        return Optional.empty();
    }

    public User getCurrentUserOrError() {
        return getCurrentUser().orElseThrow(UserNotFoundException::new);
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> registerUser(SignUpDto dto) {
        findByEmail(dto.getEmail()).ifPresent(s -> {
            throw new BadRequestException("Пользователь с данным Email уже зарегистрирован");
        });
        User user = new User();
        user.setName(dto.getName().toLowerCase());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setProvider(AuthProvider.LOCAL);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return Optional.of(userRepository.save(user));
    }

    public User registerUser(OAuth2UserInfo oAuth2UserInfo, AuthProvider provider) {
        User user = new User();
        user.setProvider(provider);
        user.setRole(Role.ROLE_USER);
        user.setName(oAuth2UserInfo.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(oAuth2UserInfo.getEmail()));
        user.setEmail(oAuth2UserInfo.getEmail().toLowerCase());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
    }

    public User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
