package ru.malkiev.blog.api.controller;

import static ru.malkiev.blog.domain.model.Role.ROLE_ADMIN;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.api.dto.UserEditDto;
import ru.malkiev.blog.api.mapper.UserMapper;
import ru.malkiev.blog.api.model.UserDetailModel;
import ru.malkiev.blog.api.model.UserModel;
import ru.malkiev.blog.application.exception.UserNotFoundException;
import ru.malkiev.blog.application.operation.EditUser;
import ru.malkiev.blog.domain.repository.UserRepository;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserRepository repository;
  private final EditUser editOperation;
  private final UserMapper mapper;

  @GetMapping("/users")
  @Secured("ROLE_ADMIN")
  public ResponseEntity<List<UserModel>> getAllUsers() {
    return Optional.of(repository.findAll())
        .map(mapper::toCollectionModel)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<UserDetailModel> getOne(@PathVariable int id) {
    return repository.findById(id)
        .map(mapper::toDetailModel)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  @GetMapping("users/admins")
  public ResponseEntity<List<UserModel>> getAdmins() {
    return Optional.ofNullable(repository.findAllByRole(ROLE_ADMIN))
        .map(mapper::toCollectionModel)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/users/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDetailModel> edit(@PathVariable int id,
      @Valid @RequestBody UserEditDto dto) {
    return repository.findById(id)
        .map(user -> Pair.of(dto, user))
        .map(editOperation)
        .map(repository::save)
        .map(mapper::toDetailModel)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new UserNotFoundException(id));
  }
}
