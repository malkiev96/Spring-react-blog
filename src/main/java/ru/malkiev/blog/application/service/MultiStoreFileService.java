package ru.malkiev.blog.application.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import ru.malkiev.blog.application.service.FileService.StorageContinuation;

/**
 * Сервис управления временными файлами. Различает несколько логических файловых хранилищ.
 */
public interface MultiStoreFileService {

  Path storeFile(DocumentStore storeId, InputStreamSource file, String filename, Object... context);

  Path storeFile(DocumentStore storeId, InputStream file, String filename, Object... context);

  Resource getFileAsResource(DocumentStore storeId, Path path);

  InputStream getFileAsInputStream(DocumentStore storeId, Path path);

  Path processFile(
      DocumentStore storeId,
      Path path,
      BiConsumer<InputStream, OutputStream> processor,
      String newFilename,
      Object... context
  );

  boolean isStored(DocumentStore storeId, Path path);

  boolean deleteIfExists(DocumentStore storeId, Path path);

  StorageContinuation<Path> take(DocumentStore storeId, Consumer<OutputStream> writer);

}
