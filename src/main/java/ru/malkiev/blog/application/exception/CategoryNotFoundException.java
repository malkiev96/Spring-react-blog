package ru.malkiev.blog.application.exception;

public class CategoryNotFoundException extends NotFoundException {

  public CategoryNotFoundException(int id) {
    super("Category not found by id: " + id);
  }
}
