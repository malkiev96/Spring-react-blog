package ru.malkiev.springsocial.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.springsocial.entity.Category;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.entity.Tag;
import ru.malkiev.springsocial.entity.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAllByCreatedBy(User user, Pageable pageable);

    Page<Post> findAllByTagsIn(List<Tag> tags, Pageable pageable);

    Page<Post> findAllByCategoryIn(List<Category> categories, Pageable pageable);
}
