package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.malkiev.springsocial.entity.AuthProvider;
import ru.malkiev.springsocial.entity.Role;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.exception.BadRequestException;
import ru.malkiev.springsocial.payload.ApiResponse;
import ru.malkiev.springsocial.payload.AuthResponse;
import ru.malkiev.springsocial.payload.LoginRequest;
import ru.malkiev.springsocial.payload.SignUpRequest;
import ru.malkiev.springsocial.repository.UserRepository;
import ru.malkiev.springsocial.security.TokenProvider;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
        if (userRepository.existsByEmail(signUpRequest.getEmail().toLowerCase())) {
            throw new BadRequestException("Пользователь с данным Email уже зарегистрирован");
        }

        User user = new User();
        user.setName(signUpRequest.getName().toLowerCase());
        user.setEmail(signUpRequest.getEmail().toLowerCase());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.LOCAL);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Пользователь успешно зарегистрирован"));
    }

}
