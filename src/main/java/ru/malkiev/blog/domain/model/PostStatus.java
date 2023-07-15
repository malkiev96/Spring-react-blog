package ru.malkiev.blog.domain.model;

public enum PostStatus {

  /**
   * Пост создан, но не опубликован
   */
  CREATED,

  /**
   * Пост опубликован
   */
  PUBLISHED,

  /**
   * Пост удален
   */
  DELETED,

  /**
   * Пост заблокирован
   */
  BLOCKED
}
