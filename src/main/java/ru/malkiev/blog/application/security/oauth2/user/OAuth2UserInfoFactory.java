package ru.malkiev.blog.application.security.oauth2.user;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.malkiev.blog.application.exception.OAuth2AuthenticationException;
import ru.malkiev.blog.domain.model.AuthProvider;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2UserInfoFactory {

  public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
      Map<String, Object> attributes) {
    if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.name())) {
      return new GoogleOAuth2UserInfo(attributes);
    } else if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
      return new GithubOAuth2UserInfo(attributes);
    } else {
      throw new OAuth2AuthenticationException(
          "Авторизация через " + registrationId + " не поддерживается");
    }
  }
}
