package ru.malkiev.springsocial.security.oauth2;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.malkiev.springsocial.entity.AuthProvider;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.exception.OAuth2AuthenticationProcessingException;
import ru.malkiev.springsocial.security.UserPrincipal;
import ru.malkiev.springsocial.security.oauth2.user.OAuth2UserInfo;
import ru.malkiev.springsocial.security.oauth2.user.OAuth2UserInfoFactory;
import ru.malkiev.springsocial.service.UserService;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
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
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        User current = userService.findByEmail(oAuth2UserInfo.getEmail()).map(user -> {
            if (!user.getProvider().equals(AuthProvider.valueOf(
                    oAuth2UserRequest
                            .getClientRegistration()
                            .getRegistrationId()
                            .toUpperCase()
            ))) throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                    user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
            return userService.updateExistingUser(user, oAuth2UserInfo);
        }).orElseGet(() -> registerNewUser(oAuth2UserRequest, oAuth2UserInfo));

        return UserPrincipal.create(current, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        AuthProvider provider = AuthProvider
                .valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        return userService.registerUser(oAuth2UserInfo, provider);
    }

}
