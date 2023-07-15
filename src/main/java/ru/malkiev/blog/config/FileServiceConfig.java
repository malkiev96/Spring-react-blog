package ru.malkiev.blog.config;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malkiev.blog.application.service.DocumentStore;
import ru.malkiev.blog.application.service.impl.DefaultFileService;

@Configuration
public class FileServiceConfig {

  @Value("${file.mount.basePath}")
  private Path basePath;

  @Bean
  public DefaultFileService defaultFileService(
      @Value("${file.documentsPath}") Path documentsPath,
      @Value("${file.documentsCachePath}") Path documentsCachePath
  ) {
    return DefaultFileService.builder()
        .withStore(DocumentStore.DOCUMENTS, basePath.resolve(documentsPath))
        .withStore(DocumentStore.DOCUMENTS_CACHE, basePath.resolve(documentsCachePath))
        .andDefaultStore(DocumentStore.DOCUMENTS);
  }

}
