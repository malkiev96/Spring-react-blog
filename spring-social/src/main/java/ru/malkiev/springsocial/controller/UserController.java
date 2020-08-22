package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.springsocial.assembler.UserAssembler;
import ru.malkiev.springsocial.assembler.UserDetailAssembler;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.exception.UserNotFoundException;
import ru.malkiev.springsocial.model.UserDetailModel;
import ru.malkiev.springsocial.model.UserModel;
import ru.malkiev.springsocial.repository.UserRepository;
import ru.malkiev.springsocial.security.CurrentUser;
import ru.malkiev.springsocial.security.UserPrincipal;

import java.util.Optional;

import static ru.malkiev.springsocial.entity.Role.ROLE_ADMIN;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserRepository repository;
    private final UserDetailAssembler detailAssembler;
    private final UserAssembler userAssembler;

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userPrincipal.getUser();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetailModel> getOne(@PathVariable int id) {
        return repository.findById(id)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("users/admins")
    public ResponseEntity<CollectionModel<UserModel>> getAdmins() {
        return Optional.ofNullable(repository.findAllByRole(ROLE_ADMIN))
                .map(userAssembler::toCollectionModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDetailModel> edit(@PathVariable int id,
                                                @RequestBody UserDetailModel model) {
        return repository.findById(id)
                .map(user -> {
                    if (model.getName() != null) user.setName(model.getName());
                    if (model.getImageUrl() != null) user.setImageUrl(model.getImageUrl());
                    if (model.getBirthDate() != null) user.setBirthDate(model.getBirthDate());
                    if (model.getCity() != null) user.setCity(model.getCity());
                    if (model.getAbout() != null) user.setAbout(model.getAbout());
                    return user;
                })
                .map(repository::save)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
