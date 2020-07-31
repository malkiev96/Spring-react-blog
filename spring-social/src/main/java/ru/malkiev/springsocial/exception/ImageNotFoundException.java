package ru.malkiev.springsocial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(int id) {
        super("Image not found by id: " + id);
    }

    public ImageNotFoundException(String filename) {
        super("Image not found by name: " + filename);
    }
}
