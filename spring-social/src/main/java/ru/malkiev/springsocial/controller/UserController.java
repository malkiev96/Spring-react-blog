package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.springsocial.assembler.UserDetailAssembler;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.exception.ResourceNotFoundException;
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
    public ResponseEntity<UserDetailModel> getOne(@PathVariable int id){
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new ResourceNotFoundException("User","id",id));
    }
}
