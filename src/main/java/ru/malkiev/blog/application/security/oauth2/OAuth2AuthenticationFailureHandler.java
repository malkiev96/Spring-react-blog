package ru.malkiev.blog.application.security.oauth2;

import static ru.malkiev.blog.application.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.malkiev.blog.application.security.util.CookieUtils;

@Component
@AllArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception) throws IOException {
    String targetUrl = CookieUtils
        .getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue)
        .orElse(("/"));

    targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("error", exception.getLocalizedMessage())
        .build().toUriString();
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request,
        response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
