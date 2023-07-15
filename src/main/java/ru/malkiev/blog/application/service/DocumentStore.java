package ru.malkiev.blog.application.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentStore {

  /**
   * Хранилище документов
   */
  DOCUMENTS(StorageStrategy.RANDOM_DIRECTORY),

  /**
   * Кэш документов
   */
  DOCUMENTS_CACHE(StorageStrategy.CONTEXT_NAMED_DIRECTORY);

  private final StorageStrategy storageStrategy;

}
