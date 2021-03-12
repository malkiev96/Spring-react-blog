package ru.malkiev.blog.security.oauth2;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.malkiev.blog.entity.AuthProvider;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.exception.OAuth2AuthenticationException;
import ru.malkiev.blog.security.CurrentUser;
import ru.malkiev.blog.security.oauth2.user.OAuth2UserInfo;
import ru.malkiev.blog.security.oauth2.user.OAuth2UserInfoFactory;
import ru.malkiev.blog.service.UserService;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(
                        oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                        oAuth2User.getAttributes()
                );
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        AuthProvider provider = getAuthProvider(oAuth2UserRequest);
        User current = userService.findByEmail(oAuth2UserInfo.getEmail()).map(user -> {
            if (!user.getProvider().equals(provider))
                throw new OAuth2AuthenticationException(
                        String.format(
                                "Looks like you're signed up with %s account. " +
                                        "Please use your %s account to login.",
                                user.getProvider(), user.getProvider())
                );
            return userService.updateExistingUser(user, oAuth2UserInfo);
        }).orElseGet(() -> userService.registerUser(oAuth2UserInfo, provider));

        return CurrentUser.create(current, oAuth2User.getAttributes());
    }

    private AuthProvider getAuthProvider(OAuth2UserRequest oAuth2UserRequest) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase();
        return AuthProvider.valueOf(registrationId);
    }

}
