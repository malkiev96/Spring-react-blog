package ru.malkiev.springsocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.springsocial.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
}
