package ru.malkiev.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.PostRating;
import ru.malkiev.blog.entity.User;

import java.util.Optional;

@Repository
public interface PostRatingRepository extends JpaRepository<PostRating, Integer> {

    Optional<PostRating> findByCreatedByAndPost(User createdBy, Post post);

    @Query("select avg(pr.star) from PostRating pr where pr.post = ?1")
    Double getAvgRatingByPost(Post post);
}
