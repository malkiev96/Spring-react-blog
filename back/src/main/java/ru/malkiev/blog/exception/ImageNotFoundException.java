package ru.malkiev.blog.exception;

public class ImageNotFoundException extends NotFoundException {
    public ImageNotFoundException(int id) {
        super("Image not found by id: " + id);
    }

    public ImageNotFoundException(String filename) {
        super("Image not found by name: " + filename);
    }
}
