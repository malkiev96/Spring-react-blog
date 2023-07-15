package ru.malkiev.blog.domain.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.blog.domain.entity.Comment;
import ru.malkiev.blog.domain.entity.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

  List<Comment> findAllByPostAndParentIsNull(Post post, Sort s);
}
