package ru.malkiev.blog.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationException extends AuthenticationException {
    public OAuth2AuthenticationException(String msg) {
        super(msg);
    }
}
