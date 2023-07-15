package ru.malkiev.blog.application.exception;

public class DocumentNotFoundException extends NotFoundException {

  public DocumentNotFoundException(Long id) {
    super("Document not found by id: " + id);
  }
}
