package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.UserDetailAssembler;
import ru.malkiev.blog.dto.LoginDto;
import ru.malkiev.blog.dto.SignUpDto;
import ru.malkiev.blog.exception.BadRequestException;
import ru.malkiev.blog.model.UserDetailModel;
import ru.malkiev.blog.security.CurrentUser;
import ru.malkiev.blog.security.TokenProvider;
import ru.malkiev.blog.security.UserPrincipal;
import ru.malkiev.blog.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserDetailAssembler userDetailAssembler;

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDetailModel> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userDetailAssembler.toModel(userPrincipal.getUser()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDetailModel> registerUser(@Valid @RequestBody SignUpDto signUpDto) {
        userService.findByEmail(signUpDto.getEmail()).ifPresent(s -> {
            throw new BadRequestException("Пользователь с данным Email уже зарегистрирован");
        });
        return userService.registerUser(signUpDto)
                .map(userDetailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Data
    private static class AuthResponse {
        private String accessToken;
        private String tokenType = "Bearer";

        public AuthResponse(String accessToken) {
            this.accessToken = accessToken;
        }
    }


}
