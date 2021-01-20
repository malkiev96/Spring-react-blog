package ru.malkiev.blog.entity;

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
    DELETED
}
