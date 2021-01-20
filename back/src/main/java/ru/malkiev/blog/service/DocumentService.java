package ru.malkiev.blog.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.malkiev.blog.entity.Document;
import ru.malkiev.blog.entity.DocumentType;
import ru.malkiev.blog.repository.DocumentRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;

    @SneakyThrows
    public Document save(MultipartFile file, DocumentType type) {
        Document document = new Document();
        document.setFilename(file.getOriginalFilename());
        document.setBody(file.getBytes());
        document.setType(type);
        return repository.save(document);
    }

    public Optional<Document> findById(Long id) {
        return repository.findById(id);
    }
}