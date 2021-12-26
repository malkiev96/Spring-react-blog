package ru.malkiev.blog.exception;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(int id) {
        super("Comment not found by id: " + id);
    }
}
