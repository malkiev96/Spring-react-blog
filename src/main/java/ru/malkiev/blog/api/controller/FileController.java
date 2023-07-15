package ru.malkiev.blog.api.controller;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import ru.malkiev.blog.api.mapper.DocumentMapper;
import ru.malkiev.blog.api.model.DocumentModel;
import ru.malkiev.blog.api.model.TempFileModel;
import ru.malkiev.blog.application.service.DocumentService;
import ru.malkiev.blog.application.service.StorePath;
import ru.malkiev.blog.application.service.impl.DefaultFileService;
import ru.malkiev.blog.domain.model.DocumentType;

@RestController
@AllArgsConstructor
public class FileController {

  private final DefaultFileService fileService;
  private final DocumentService documentService;
  private final DocumentMapper mapper;

  @GetMapping("/files/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam String fileId) {
    return Optional.of(fileId)
        .map(id -> UriUtils.decode(id, StandardCharsets.UTF_8))
        .map(StorePath::parseOrThrow)
        .map(fileService::getFileAsResource)
        .map(resource -> {
          String filename = resource.getFilename();
          Assert.notNull(filename, "FileName can't be null");
          DocumentType documentType = DocumentType.of(filename);
          String encodedFileName = UriUtils.encode(filename, StandardCharsets.UTF_8);

          HttpHeaders headers = new HttpHeaders();
          if (!documentType.isImage()) {
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(encodedFileName, StandardCharsets.UTF_8)
                .build();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
          }
          return ResponseEntity.ok()
              .headers(headers)
              .contentType(documentType.getMediaType())
              .body(resource);
        })
        .orElse(ResponseEntity.noContent().build());
  }

  @PostMapping("/files")
  public ResponseEntity<TempFileModel> uploadTempFile(MultipartFile file) {
    String filename = file.getOriginalFilename();
    StorePath fileId = fileService.storeFile(file, filename);
    return ResponseEntity.ok(
        new TempFileModel(UriUtils.encode(fileId.toString(), StandardCharsets.UTF_8), filename)
    );
  }

  @PostMapping("/documents")
  public ResponseEntity<DocumentModel> createDocument(@RequestParam String fileId) {
    return Optional.of(fileId)
        .map(id -> UriUtils.decode(id, StandardCharsets.UTF_8))
        .map(StorePath::parseOrThrow)
        .map(documentService::createDocument)
        .map(mapper::toModel)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.noContent().build());
  }

}
