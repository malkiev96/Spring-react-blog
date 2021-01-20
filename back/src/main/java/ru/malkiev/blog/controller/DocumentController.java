package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.util.InMemoryResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.malkiev.blog.assembler.DocumentAssembler;
import ru.malkiev.blog.entity.Document;
import ru.malkiev.blog.entity.DocumentType;
import ru.malkiev.blog.exception.DocumentNotFoundException;
import ru.malkiev.blog.model.DocumentModel;
import ru.malkiev.blog.service.DocumentService;

import javax.servlet.http.HttpServletResponse;

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

    @SneakyThrows
    @GetMapping(value = "/documents/{id}/download")
    public ResponseEntity<InMemoryResource> download(@PathVariable Long id, HttpServletResponse response) {
        return documentService.findById(id)
                .map(Document::getBody)
                .map(InMemoryResource::new)
                .map(resource -> ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource))
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    @PostMapping("/documents")
    public ResponseEntity<DocumentModel> upload(@RequestParam("file") MultipartFile file,
                                                @RequestParam("type") DocumentType type) {
        return ResponseEntity.ok(assembler.toModel(documentService.save(file, type)));
    }
}
