package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.CommentAssembler;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.exception.CommentNotFoundException;
import ru.malkiev.blog.exception.PostNotFoundException;
import ru.malkiev.blog.model.CommentModel;
import ru.malkiev.blog.operation.CommentCreateOperation;
import ru.malkiev.blog.operation.CommentDeleteOperation;
import ru.malkiev.blog.operation.CommentReplyOperation;
import ru.malkiev.blog.repository.CommentRepository;
import ru.malkiev.blog.repository.PostRepository;
import ru.malkiev.blog.security.CurrentUser;
import ru.malkiev.blog.security.UserPrincipal;

@RestController
@AllArgsConstructor
public class CommentController {

    private final CommentRepository repository;
    private final CommentCreateOperation createOperation;
    private final CommentReplyOperation replyOperation;
    private final CommentDeleteOperation deleteOperation;
    private final PostRepository postRepository;
    private final CommentAssembler assembler;

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentModel> getOne(@PathVariable int id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    @GetMapping("/comments/post/{id}")
    public CollectionModel<CommentModel> getAllOfPosts(@PathVariable int id) {
        return postRepository.findById(id)
                .map(post -> repository.findAllByPostAndParentIsNull(post,
                        Sort.by("createdDate").descending()))
                .map(assembler::toCollectionModel)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    @PostMapping("/comments/post/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentModel> create(@PathVariable int id, @RequestBody String message) {
        return postRepository.findById(id)
                .map(post -> Pair.of(post, message))
                .map(createOperation)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    @PostMapping("/comments/post/{id}/reply/{parentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentModel> reply(@PathVariable int id,
                                              @PathVariable int parentId,
                                              @RequestBody String message) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        return repository.findById(parentId)
                .map(parent -> Pair.of(Pair.of(post, message), parent))
                .map(replyOperation)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CommentNotFoundException(parentId));
    }

    @DeleteMapping("/comments/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentModel> delete(@PathVariable int id,
                                               @CurrentUser UserPrincipal principal) {
        return repository.findById(id)
                .filter(comment -> comment.canEdit(principal.getUser()))
                .map(deleteOperation)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }
}
