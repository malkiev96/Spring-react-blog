package ru.malkiev.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.malkiev.blog.entity.AuthProvider;
import ru.malkiev.blog.entity.Role;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.model.payload.SignUpRequest;
import ru.malkiev.blog.repository.UserRepository;
import ru.malkiev.blog.security.UserPrincipal;
import ru.malkiev.blog.security.oauth2.user.OAuth2UserInfo;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            return Optional.of(((UserPrincipal) principal).getUser());
        }
        return Optional.empty();
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> registerUser(SignUpRequest request) {
        User user = new User();
        user.setName(request.getName().toLowerCase());
        user.setEmail(request.getEmail().toLowerCase());
        user.setProvider(AuthProvider.LOCAL);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return Optional.ofNullable(userRepository.save(user));
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
