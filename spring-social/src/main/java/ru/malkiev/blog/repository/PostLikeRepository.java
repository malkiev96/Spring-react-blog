package ru.malkiev.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.PostLike;
import ru.malkiev.blog.entity.User;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {

    Integer countByPost(Post post);

    Optional<PostLike> findByCreatedByAndPost(User createdBy, Post post);
}
