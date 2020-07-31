package ru.malkiev.springsocial.controller;

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
import ru.malkiev.springsocial.exception.BadRequestException;
import ru.malkiev.springsocial.model.payload.ApiResponse;
import ru.malkiev.springsocial.model.payload.AuthResponse;
import ru.malkiev.springsocial.model.payload.LoginRequest;
import ru.malkiev.springsocial.model.payload.SignUpRequest;
import ru.malkiev.springsocial.security.TokenProvider;
import ru.malkiev.springsocial.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.findByEmail(signUpRequest.getEmail()).ifPresent(s -> {
            throw new BadRequestException("Пользователь с данным Email уже зарегистрирован");
        });

        return userService.registerUser(signUpRequest)
                .map(user -> ResponseEntity
                        .accepted()
                        .body(new ApiResponse(true, "Пользователь успешно зарегистрирован")))
                .orElse(ResponseEntity.badRequest().build());
    }

}
