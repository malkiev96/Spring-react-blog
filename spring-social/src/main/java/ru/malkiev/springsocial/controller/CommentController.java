package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.springsocial.assembler.CommentAssembler;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.exception.ResourceNotFoundException;
import ru.malkiev.springsocial.model.CommentModel;
import ru.malkiev.springsocial.security.CurrentUser;
import ru.malkiev.springsocial.security.UserPrincipal;
import ru.malkiev.springsocial.service.CommentService;
import ru.malkiev.springsocial.service.PostService;

@RestController
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final CommentAssembler assembler;

    @GetMapping("/comments/post/{id}")
    public CollectionModel<CommentModel> getAllOfPosts(@PathVariable int id) {
        return postService.findById(id)
                .map(post -> commentService.getComments(post, Sort.by("createdDate").descending()))
                .map(assembler::toCollectionModel)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }

    @PostMapping("/comments/post/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentModel> create(@PathVariable int id, @RequestBody String message) {
        return postService.findById(id)
                .map(p -> commentService.create(p, message))
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }

    @PostMapping("/comments/post/{id}/reply/{parentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentModel> reply(@PathVariable int id,
                                              @PathVariable int parentId,
                                              @RequestBody String message) {
        Post post = postService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return commentService.getComment(parentId)
                .map(parent -> commentService.reply(post, message, parent))
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", parentId));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentModel> getOne(@PathVariable int id) {
        return commentService.getComment(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
    }

    @DeleteMapping("/comments/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentModel> delete(@PathVariable int id, @CurrentUser UserPrincipal principal) {
        return commentService.getComment(id)
                .filter(comment -> comment.canEdit(principal.getUser()))
                .map(commentService::delete)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
    }
}
