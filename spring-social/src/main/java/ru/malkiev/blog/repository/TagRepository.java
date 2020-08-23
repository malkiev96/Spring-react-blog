package ru.malkiev.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.blog.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
}
