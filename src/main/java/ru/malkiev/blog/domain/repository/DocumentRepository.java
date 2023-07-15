package ru.malkiev.blog.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.malkiev.blog.domain.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
