package ru.malkiev.blog.api.controller;

import javax.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.api.dto.LoginDto;
import ru.malkiev.blog.api.dto.SignUpDto;
import ru.malkiev.blog.api.mapper.UserMapper;
import ru.malkiev.blog.api.model.UserDetailModel;
import ru.malkiev.blog.application.security.CustomUserDetails;
import ru.malkiev.blog.application.security.TokenProvider;
import ru.malkiev.blog.application.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final TokenProvider tokenProvider;
  private final UserMapper userMapper;

  @GetMapping("/user/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDetailModel> getCurrentUser(
      @AuthenticationPrincipal CustomUserDetails currentUser) {
    return ResponseEntity.ok(userMapper.toDetailModel(currentUser.getUser()));
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
    return userService.registerUser(signUpDto)
        .map(userMapper::toDetailModel)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.badRequest().build());
  }

  @Data
  public static class AuthResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public AuthResponse(String accessToken) {
      this.accessToken = accessToken;
    }
  }


}
