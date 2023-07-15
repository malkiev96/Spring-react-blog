package ru.malkiev.blog.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import ru.malkiev.blog.application.exception.FileStorageException;

/**
 * Способ хранения файла в хранилище
 */
@RequiredArgsConstructor
public enum StorageStrategy {

  /**
   * Хранить файл в общем хранилище
   */
  BASIC((root, ignored) -> root),

  /**
   * Хранить файл в отдельном каталоге со случайным именем
   */
  RANDOM_DIRECTORY(StorageStrategy::randomDir),

  /**
   * Хранить файл в отдельном каталоге с именем, заданным контекстом
   */
  CONTEXT_NAMED_DIRECTORY(StorageStrategy::namedDir);

  private final BiFunction<Path, Object[], Path> makeStoragePath;

  public Path apply(Path root, String filename, Object... context) {
    Path path = makeStoragePath.apply(root, context);
    return Paths.get(path.toString(), filename);
  }

  private static Path randomDir(Path root, Object... ignored) {
    try {
      return Files.createTempDirectory(root, null);
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }

  private static Path namedDir(Path root, Object... context) {
    String[] names = Optional.ofNullable(context)
        .map(ctx -> Arrays.stream(ctx).filter(Objects::nonNull).map(Object::toString))
        .orElseGet(Stream::empty)
        .toArray(String[]::new);
    return Paths.get(root.toString(), names);
  }

}
