package ru.malkiev.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.malkiev.blog.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
