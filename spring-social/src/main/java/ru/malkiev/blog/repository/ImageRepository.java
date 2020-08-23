package ru.malkiev.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.malkiev.blog.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>, JpaSpecificationExecutor<Image> {
}
