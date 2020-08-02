package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.springsocial.assembler.UserDetailAssembler;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.exception.UserNotFoundException;
import ru.malkiev.springsocial.model.UserDetailModel;
import ru.malkiev.springsocial.repository.UserRepository;
import ru.malkiev.springsocial.security.CurrentUser;
import ru.malkiev.springsocial.security.UserPrincipal;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserRepository repository;
    private final UserDetailAssembler assembler;

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userPrincipal.getUser();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetailModel> getOne(@PathVariable int id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
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
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new UserNotFoundException(id));
    }
}
