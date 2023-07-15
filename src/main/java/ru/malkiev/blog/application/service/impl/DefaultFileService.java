package ru.malkiev.blog.application.service.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import ru.malkiev.blog.application.service.DocumentStore;
import ru.malkiev.blog.application.service.FileService;
import ru.malkiev.blog.application.service.MultiStoreFileService;
import ru.malkiev.blog.application.service.StorePath;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultFileService implements FileService<StorePath> {

  private final DocumentStore defaultStore;
  private final MultiStoreFileService service;

  public StorePath storeFile(DocumentStore storeId, InputStreamSource file, String filename,
      Object... context) {
    Path path = service.storeFile(storeId, file, filename, context);
    return new StorePath(storeId, path);
  }

  @Override
  public StorePath storeFile(InputStreamSource file, String filename, Object... context) {
    return storeFile(defaultStore, file, filename, context);
  }

  public StorePath storeFile(DocumentStore storeId, InputStream file, String filename,
      Object... context) {
    Path path = service.storeFile(storeId, file, filename, context);
    return new StorePath(storeId, path);
  }

  @Override
  public StorePath storeFile(InputStream file, String filename, Object... context) {
    return storeFile(defaultStore, file, filename, context);
  }

  @Override
  public Resource getFileAsResource(StorePath fileId) {
    return service.getFileAsResource(fileId.getStoreId(), fileId.getPath());
  }

  @Override
  public InputStream getFileAsInputStream(StorePath fileId) {
    return service.getFileAsInputStream(fileId.getStoreId(), fileId.getPath());
  }

  @Override
  public StorePath processFile(
      StorePath fileId,
      BiConsumer<InputStream, OutputStream> processor,
      String newFilename,
      Object... context
  ) {
    DocumentStore storeId = fileId.getStoreId();
    Path path = service.processFile(storeId, fileId.getPath(), processor, newFilename, context);
    return new StorePath(storeId, path);
  }

  @Override
  public boolean isStored(StorePath fileId) {
    return service.isStored(fileId.getStoreId(), fileId.getPath());
  }

  @Override
  public boolean deleteIfExists(StorePath fileId) {
    return service.deleteIfExists(fileId.getStoreId(), fileId.getPath());
  }

  @Override
  public StorageContinuation<StorePath> take(Consumer<OutputStream> writer) {
    return (filename, context) -> {
      DocumentStore storeId = defaultStore;
      Path path = service.take(storeId, writer).thenStoreAs(filename, context);
      return new StorePath(storeId, path);
    };
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final Map<DocumentStore, FileService<Path>> stores = new EnumMap<>(DocumentStore.class);

    public Builder withStore(DocumentStore key, Path rootPath) {
      this.stores.put(key, new PlainFileService(rootPath, key.getStorageStrategy()));
      return this;
    }

    public DefaultFileService andDefaultStore(DocumentStore defaultStore) {
      MultiStoreFileServiceImpl multiStore = new MultiStoreFileServiceImpl(stores);
      return new DefaultFileService(defaultStore, multiStore);
    }

  }
}
