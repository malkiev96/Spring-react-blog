package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.springsocial.assembler.UserAssembler;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.exception.ResourceNotFoundException;
import ru.malkiev.springsocial.model.UserModel;
import ru.malkiev.springsocial.repository.UserRepository;
import ru.malkiev.springsocial.security.CurrentUser;
import ru.malkiev.springsocial.security.UserPrincipal;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserRepository repository;
    private final UserAssembler assembler;

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userPrincipal.getUser();
    }

    @GetMapping("/users/{id}")
    public UserModel getOne(@PathVariable int id){
        User user = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User","id",id));
        return assembler.toModel(user);
    }
}
