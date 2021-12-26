package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.util.InMemoryResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.malkiev.blog.assembler.DocumentAssembler;
import ru.malkiev.blog.entity.Document;
import ru.malkiev.blog.entity.DocumentType;
import ru.malkiev.blog.exception.DocumentNotFoundException;
import ru.malkiev.blog.model.DocumentModel;
import ru.malkiev.blog.service.DocumentService;

import java.nio.charset.StandardCharsets;

@RestController
@AllArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentAssembler assembler;

    @GetMapping("/documents/{id}")
    public ResponseEntity<DocumentModel> getOne(@PathVariable Long id) {
        return documentService.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    @DeleteMapping("/documents/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Document document = documentService.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
        documentService.delete(document);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping(value = "/documents/{id}/download")
    public ResponseEntity<InMemoryResource> download(@PathVariable Long id) {
        return documentService.findById(id)
                .map(this::getResponseEntity)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    @PostMapping("/documents")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DocumentModel> upload(@RequestParam("file") MultipartFile file,
                                                @RequestParam("type") DocumentType type) {
        return ResponseEntity.ok(assembler.toModel(documentService.save(file, type)));
    }

    private ResponseEntity<InMemoryResource> getResponseEntity(Document document) {
        MediaType mediaType = MediaType.IMAGE_JPEG;
        HttpHeaders headers = new HttpHeaders();
        if (document.getType() != DocumentType.IMAGE) {
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(document.getFilename(), StandardCharsets.UTF_8)
                    .build();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .body(new InMemoryResource(document.getBody()));
    }
}
