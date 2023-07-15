package ru.malkiev.blog.application.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * Пара из идентификаторов хранилища и пути к файлу.
 * <p>
 * Допускает конвертацию в/из строки.
 */
@Data
public class StorePath {

  private static final String DELIMITER = ":";
  private static final Pattern PATTERN = Pattern.compile(
      "(?<storeId>.+)" + DELIMITER + "(?<path>.+)");

  private final DocumentStore storeId;
  private final Path path;


  @Override
  public String toString() {
    return storeId + DELIMITER + path;
  }


  public static Optional<StorePath> parse(@NonNull String fileId) {
    Matcher matcher = PATTERN.matcher(fileId);
    if (matcher.matches()) {
      String storeName = matcher.group("storeId");
      String pathString = matcher.group("path");
      try {
        DocumentStore store = DocumentStore.valueOf(storeName);
        Path path = Paths.get(pathString);
        return Optional.of(new StorePath(store, path
        ));
      } catch (Exception e) {
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  public static StorePath parseOrThrow(@NonNull String fileId) {
    return parse(fileId).orElseThrow(() -> new IllegalArgumentException("Wrong fileId: " + fileId));
  }

}
