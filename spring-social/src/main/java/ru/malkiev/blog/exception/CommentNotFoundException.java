package ru.malkiev.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(int id) {
        super("Comment not found by id: " + id);
    }
}
