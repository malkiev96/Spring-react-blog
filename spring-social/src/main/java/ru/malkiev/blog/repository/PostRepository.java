package ru.malkiev.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.malkiev.blog.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>,
        JpaSpecificationExecutor<Post> {
}
