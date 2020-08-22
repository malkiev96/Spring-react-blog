package ru.malkiev.springsocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.springsocial.entity.ContactMessage;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Integer> {
}
