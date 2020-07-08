package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.springsocial.assembler.CommentAssembler;
import ru.malkiev.springsocial.entity.Comment;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.exception.ResourceNotFoundException;
import ru.malkiev.springsocial.model.CommentModel;
import ru.malkiev.springsocial.repository.CommentRepository;
import ru.malkiev.springsocial.repository.PostRepository;
import ru.malkiev.springsocial.security.CurrentUser;
import ru.malkiev.springsocial.security.UserPrincipal;

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

    @DeleteMapping("/comments/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteComment(@PathVariable int id,
                                           @CurrentUser UserPrincipal principal) {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        if (comment.canEdit(principal.getUser())) {
            repository.delete(comment);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.noContent().build();
    }
}
