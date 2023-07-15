package ru.malkiev.blog.application.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;

public interface FileService<I> {

  /**
   * Положить файл на хранение
   *
   * @return идентификатор файла в хранилище
   */
  I storeFile(InputStreamSource file, String filename, Object... context);

  /**
   * Положить файл на хранение
   *
   * @return идентификатор файла в хранилище
   */
  I storeFile(InputStream file, String filename, Object... context);

  /**
   * Считать файл из хранилища
   *
   * @param fileId идентификатор файла в хранилище
   */
  Resource getFileAsResource(I fileId);

  /**
   * Считать файл из хранилища
   *
   * @param fileId идентификатор файла в хранилище
   */
  InputStream getFileAsInputStream(I fileId);

  /**
   * Создать новый файл в результате обработки имеющегося в хранилище
   */
  I processFile(
      I fileId,
      BiConsumer<InputStream, OutputStream> processor,
      String newFilename,
      Object... context
  );

  /**
   * Проверить наличие файла в хранилище
   */
  boolean isStored(I fileId);

  /**
   * Удалить файл из хранилища
   */
  boolean deleteIfExists(I fileId);

  /**
   * Запись данных для дальнейшего сохранения в хранилище
   *
   * @param writer процедура записи данных
   * @return объект-продолжение, реализующий сохранение данных в хранилище
   */
  StorageContinuation<I> take(Consumer<OutputStream> writer);


  interface StorageContinuation<X> {

    X thenStoreAs(String filename, Object... context);
  }

}
