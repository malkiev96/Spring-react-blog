package ru.malkiev.blog.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(int id) {
        super("User not found by id: " + id);
    }

    public UserNotFoundException() {
        super("User not found");
    }
}
