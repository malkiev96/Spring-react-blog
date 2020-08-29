package ru.malkiev.blog.exception;

public class PostNotFoundException extends NotFoundException {
    public PostNotFoundException(int id) {
        super("Post not found by id: " + id);
    }
}
