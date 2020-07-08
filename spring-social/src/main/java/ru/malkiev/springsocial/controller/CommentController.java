package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
        return assembler.toCollectionModel(repository.findAllByPostAndParentIsNull(post, Sort.by("createdDate").descending()));
    }

    @PostMapping("/comments/post/{id}")
    @PreAuthorize("isAuthenticated()")
    public CommentModel createComment(@PathVariable int id,
                                           @RequestBody String message) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setMessage(message);

        return assembler.toModel(repository.save(comment));
    }

    @PostMapping("/comments/post/{id}/reply/{parentId}")
    @PreAuthorize("isAuthenticated()")
    public CommentModel replyComment(@PathVariable int id,
                                          @PathVariable int parentId,
                                          @RequestBody String message) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        Comment comment = new Comment();
        Comment parent = repository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", parentId));
        comment.setParent(parent);
        comment.setPost(post);
        comment.setMessage(message);

        return assembler.toModel(repository.save(comment));
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
            comment.setMessage("Сообщено удалено");
            comment.setDeleted(true);
            repository.save(comment);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.noContent().build();
    }
}
