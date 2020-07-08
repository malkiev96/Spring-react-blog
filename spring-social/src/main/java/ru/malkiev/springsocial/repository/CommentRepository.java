package ru.malkiev.springsocial.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.springsocial.entity.Comment;
import ru.malkiev.springsocial.entity.Post;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByPostAndParentIsNull(Post post, Sort s);
}
