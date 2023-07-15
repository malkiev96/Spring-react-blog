package ru.malkiev.blog.application.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.malkiev.blog.api.dto.SignUpDto;
import ru.malkiev.blog.application.exception.BadRequestException;
import ru.malkiev.blog.application.security.oauth2.user.OAuth2UserInfo;
import ru.malkiev.blog.domain.entity.Document;
import ru.malkiev.blog.domain.entity.User;
import ru.malkiev.blog.domain.model.AuthProvider;
import ru.malkiev.blog.domain.model.Role;
import ru.malkiev.blog.domain.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final DocumentService documentService;

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
    Document preview = Optional.ofNullable(oAuth2UserInfo.getImageUrl())
        .flatMap(documentService::createDocumentFromUrl)
        .orElse(null);
    user.setPreview(preview);
    return userRepository.save(user);
  }

  public User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.setName(oAuth2UserInfo.getName());
    Document preview = Optional.ofNullable(oAuth2UserInfo.getImageUrl())
        .flatMap(documentService::createDocumentFromUrl)
        .orElse(null);
    existingUser.setPreview(preview);
    return userRepository.save(existingUser);
  }
}
