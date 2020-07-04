package com.example.springsocial.repository;

import com.example.springsocial.entity.Comment;
import com.example.springsocial.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByPost(Post post);
}
