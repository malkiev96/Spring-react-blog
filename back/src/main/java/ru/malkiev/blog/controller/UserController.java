package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.UserAssembler;
import ru.malkiev.blog.assembler.UserDetailAssembler;
import ru.malkiev.blog.dto.UserEditDto;
import ru.malkiev.blog.exception.UserNotFoundException;
import ru.malkiev.blog.model.UserDetailModel;
import ru.malkiev.blog.model.UserModel;
import ru.malkiev.blog.operation.UserEditOperation;
import ru.malkiev.blog.repository.UserRepository;

import javax.validation.Valid;
import java.util.Optional;

import static ru.malkiev.blog.entity.Role.ROLE_ADMIN;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserRepository repository;
    private final UserDetailAssembler detailAssembler;
    private final UserAssembler userAssembler;
    private final UserEditOperation editOperation;

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
                                                @Valid @RequestBody UserEditDto dto) {
        return repository.findById(id)
                .map(user -> Pair.of(dto, user))
                .map(editOperation)
                .map(repository::save)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
