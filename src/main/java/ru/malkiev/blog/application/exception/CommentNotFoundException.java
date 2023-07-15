package ru.malkiev.blog.application.exception;

public class CommentNotFoundException extends NotFoundException {

  public CommentNotFoundException(int id) {
    super("Comment not found by id: " + id);
  }
}
