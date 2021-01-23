package ru.malkiev.blog.exception;

public class TagNotFoundException extends NotFoundException {
    public TagNotFoundException(Integer id) {
        super("Tag not found by id: " + id);
    }
}
