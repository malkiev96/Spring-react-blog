package ru.malkiev.blog.application.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import ru.malkiev.blog.application.exception.FileStorageException;
import ru.malkiev.blog.application.service.FileService;
import ru.malkiev.blog.application.service.StorageStrategy;

@Getter
public class PlainFileService implements FileService<Path> {

  private final Path rootPath;
  private final StorageStrategy storageStrategy;

  protected PlainFileService(Path rootPath, StorageStrategy storageStrategy) {
    this.rootPath = rootPath;
    this.storageStrategy = storageStrategy;
    ensurePathExists(rootPath);
  }

  @Override
  public Path storeFile(InputStreamSource file, String filename, Object... context) {
    try (InputStream inputStream = file.getInputStream()) {
      return storeFile(inputStream, filename, context);
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }

  @Override
  public Path storeFile(InputStream file, String filename, Object... context) {
    Path filePath = getStoragePath(filename, context);
    try {
      Files.createDirectories(filePath.getParent());
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      IOUtils.copy(file, outputStream);
      return rootPath.relativize(filePath);
    } catch (Exception e) {
      throw new FileStorageException(e);
    }
  }

  @Override
  public Resource getFileAsResource(Path fileId) {
    Path storagePath = getStoragePath(fileId);
    PathResource resource = new PathResource(storagePath);
    if (resource.exists()) {
      return resource;
    } else {
      throw new FileStorageException("File not found " + fileId);
    }
  }

  @Override
  public InputStream getFileAsInputStream(Path fileId) {
    Path storagePath = getStoragePath(fileId);
    try {
      return Files.newInputStream(storagePath);
    } catch (IOException e) {
      throw new FileStorageException("File not found " + fileId);
    }
  }

  @Override
  public Path processFile(Path fileId, BiConsumer<InputStream, OutputStream> processor,
      String newFilename, Object... context) {
    String filename = StringUtils.isEmpty(newFilename)
        ? fileId.getFileName().toString()
        : newFilename;
    Path newFilePath = storeEmptyFile(filename, context);
    try (
        InputStream is = getFileAsInputStream(fileId);
        OutputStream out = getFileAsOutputStream(newFilePath);
    ) {
      processor.accept(is, out);
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
    return newFilePath;
  }

  @Override
  public boolean isStored(Path fileId) {
    return Files.exists(getStoragePath(fileId));
  }

  @Override
  public boolean deleteIfExists(Path fileId) {
    try {
      return Files.deleteIfExists(getStoragePath(fileId));
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }

  @Override
  public StorageContinuation<Path> take(Consumer<OutputStream> writer) {
    return (filename, context) -> {
      Path path = storeEmptyFile(filename, context);
      Path fullPath = getStoragePath(path);
      try (OutputStream outputStream = Files.newOutputStream(fullPath)) {
        writer.accept(outputStream);
      } catch (IOException e) {
        throw new FileStorageException(e);
      }
      return path;
    };
  }


  private Path getStoragePath(@NonNull Path relativePath) {
    return rootPath.resolve(relativePath);
  }

  private Path getStoragePath(String filename, Object... context) {
    return storageStrategy.apply(rootPath, filename, context);
  }

  private Path storeEmptyFile(String filename, Object... context) {
    Path filePath = getStoragePath(filename, context);
    try {
      Files.createDirectories(filePath.getParent());
      Files.createFile(filePath);
      return rootPath.relativize(filePath);
    } catch (IOException e) {
      throw new FileStorageException("Could not store file", e);
    }
  }

  private OutputStream getFileAsOutputStream(Path relativePath) {
    Path filePath = rootPath.resolve(relativePath).normalize();
    try {
      return Files.newOutputStream(filePath);
    } catch (IOException e) {
      throw new FileStorageException("File not found " + relativePath, e);
    }
  }

  private void ensurePathExists(Path path) {
    try {
      if (Files.notExists(path)) {
        Files.createDirectories(path);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
