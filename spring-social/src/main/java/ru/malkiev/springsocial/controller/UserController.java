package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.security.CurrentUser;
import ru.malkiev.springsocial.security.UserPrincipal;

@RestController
@AllArgsConstructor
public class UserController {

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userPrincipal.getUser();
    }
}
