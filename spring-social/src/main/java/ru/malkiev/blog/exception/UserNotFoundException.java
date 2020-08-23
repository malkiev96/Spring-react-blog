package ru.malkiev.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("User not found by id: " + id);
    }
    public UserNotFoundException(){
        super("User not found");
    }
}
