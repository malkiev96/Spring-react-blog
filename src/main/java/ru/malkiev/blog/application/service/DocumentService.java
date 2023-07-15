package ru.malkiev.blog.application.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import ru.malkiev.blog.application.exception.FileStorageException;
import ru.malkiev.blog.application.service.impl.DefaultFileService;
import ru.malkiev.blog.domain.entity.Document;
import ru.malkiev.blog.domain.model.DocumentType;
import ru.malkiev.blog.domain.repository.DocumentRepository;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {

  private final DocumentRepository repository;
  private final DefaultFileService fileService;

  public Document createDocument(@NonNull StorePath fileId) {
    Resource resource = fileService.getFileAsResource(fileId);
    if (!resource.exists() || !resource.isFile()) {
      throw new FileStorageException("File not exists: " + fileId);
    }
    String filename = resource.getFilename();
    Document document = new Document();

    document.setFilename(filename);
    document.setType(DocumentType.of(filename));
    document.setFileId(fileId.toString());

    try (InputStream inputStream = resource.getInputStream()) {
      document.setFileSize(resource.contentLength());
      document.setBody(IOUtils.toByteArray(inputStream));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return repository.save(document);
  }

  public Optional<Document> createDocumentFromUrl(@NonNull String fileUrl) {
    try {
      URL url = new URL(fileUrl);
      String filename = FilenameUtils.getName(url.getPath());
      InputStream inputStream = new BufferedInputStream(url.openStream());
      StorePath storePath = fileService.storeFile(inputStream, filename);

      Document document = createDocument(storePath);
      return Optional.of(document);
    } catch (IOException e) {
      log.warn(String.format("Could not read file from url %s", fileUrl), e);
      return Optional.empty();
    }
  }

  public List<Document> createDocumentList(List<String> fileIds) {
    return fileIds.stream()
        .filter(Objects::nonNull)
        .map(fileId -> UriUtils.decode(fileId, StandardCharsets.UTF_8))
        .map(StorePath::parseOrThrow)
        .map(this::createDocument)
        .collect(Collectors.toList());
  }

  public Document createDocumentByFileId(@NonNull String encodedFileId) {
    String decoded = UriUtils.decode(encodedFileId, StandardCharsets.UTF_8);
    StorePath storePath = StorePath.parseOrThrow(decoded);
    return createDocument(storePath);
  }

}