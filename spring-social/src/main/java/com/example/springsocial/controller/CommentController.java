package com.example.springsocial.controller;

import com.example.springsocial.assembler.CommentAssembler;
import com.example.springsocial.entity.Comment;
import com.example.springsocial.entity.Post;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.model.CommentModel;
import com.example.springsocial.repository.CommentRepository;
import com.example.springsocial.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CommentController {

    private final CommentRepository repository;
    private final PostRepository postRepository;
    private final CommentAssembler assembler;

    @GetMapping("/comments/post/{id}")
    public CollectionModel<CommentModel> getAllOfPosts(@PathVariable int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return assembler.toCollectionModel(repository.findAllByPost(post));
    }

    @GetMapping("/comments/{id}")
    public CommentModel getOne(@PathVariable int id) {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        return assembler.toModel(comment);
    }
}
