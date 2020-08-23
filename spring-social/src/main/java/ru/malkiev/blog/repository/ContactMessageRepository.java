package ru.malkiev.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.blog.entity.ContactMessage;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Integer> {
}
