package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.exception.BadRequestException;
import ru.malkiev.blog.model.payload.ApiResponse;
import ru.malkiev.blog.model.payload.AuthResponse;
import ru.malkiev.blog.model.payload.LoginDto;
import ru.malkiev.blog.model.payload.SignUpDto;
import ru.malkiev.blog.security.TokenProvider;
import ru.malkiev.blog.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDto signUpDto) {
        userService.findByEmail(signUpDto.getEmail()).ifPresent(s -> {
            throw new BadRequestException("Пользователь с данным Email уже зарегистрирован");
        });

        return userService.registerUser(signUpDto)
                .map(user -> ResponseEntity
                        .accepted()
                        .body(new ApiResponse(true, "Пользователь успешно зарегистрирован")))
                .orElse(ResponseEntity.badRequest().build());
    }

}
