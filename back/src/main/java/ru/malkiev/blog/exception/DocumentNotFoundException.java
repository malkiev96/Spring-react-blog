package ru.malkiev.blog.exception;

public class DocumentNotFoundException extends NotFoundException {
    public DocumentNotFoundException(Long id) {
        super("Document not found by id: " + id);
    }

    public DocumentNotFoundException(String filename) {
        super("Document not found by name: " + filename);
    }
}
