package ru.malkiev.blog.application.service.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import ru.malkiev.blog.application.exception.FileStorageException;
import ru.malkiev.blog.application.service.DocumentStore;
import ru.malkiev.blog.application.service.FileService;
import ru.malkiev.blog.application.service.FileService.StorageContinuation;
import ru.malkiev.blog.application.service.MultiStoreFileService;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MultiStoreFileServiceImpl implements MultiStoreFileService {

  private final Map<DocumentStore, FileService<Path>> stores;

  @Override
  public Path storeFile(DocumentStore storeId, InputStreamSource file, String filename,
      Object... context) {
    return selectStore(storeId).storeFile(file, filename, context);
  }

  @Override
  public Path storeFile(DocumentStore storeId, InputStream file, String filename,
      Object... context) {
    return selectStore(storeId).storeFile(file, filename, context);
  }

  @Override
  public Resource getFileAsResource(DocumentStore storeId, Path path) {
    return selectStore(storeId).getFileAsResource(path);
  }

  @Override
  public InputStream getFileAsInputStream(DocumentStore storeId, Path path) {
    return selectStore(storeId).getFileAsInputStream(path);
  }

  @Override
  public Path processFile(
      DocumentStore storeId,
      Path path,
      BiConsumer<InputStream, OutputStream> processor,
      String newFilename,
      Object... context) {
    return selectStore(storeId).processFile(path, processor, newFilename, context);
  }

  @Override
  public boolean isStored(DocumentStore storeId, Path path) {
    return selectStore(storeId).isStored(path);
  }

  @Override
  public boolean deleteIfExists(DocumentStore storeId, Path path) {
    return selectStore(storeId).deleteIfExists(path);
  }

  @Override
  public StorageContinuation<Path> take(DocumentStore storeId, Consumer<OutputStream> writer) {
    return selectStore(storeId).take(writer);
  }

  private FileService<Path> selectStore(DocumentStore storeId) {
    FileService<Path> fileService = stores.get(storeId);
    if (fileService == null) {
      throw new FileStorageException("Неверный идентификатор хранилища " + storeId.toString());
    }
    return fileService;
  }

}
