package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.springsocial.assembler.PostDetailAssembler;
import ru.malkiev.springsocial.exception.PostNotFoundException;
import ru.malkiev.springsocial.link.PostLinks;
import ru.malkiev.springsocial.model.PostDetailModel;
import ru.malkiev.springsocial.model.payload.PostRequest;
import ru.malkiev.springsocial.operation.*;
import ru.malkiev.springsocial.repository.PostRepository;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class PostOperationController {

    private final PostRepository repository;
    private final PostDeleteOperation deleteOperation;
    private final PostHideOperation hideOperation;
    private final PostPublishOperation publishOperation;
    private final AddLikeOperation likeOperation;
    private final AddStarOperation starOperation;
    private final PostCreateOperation createOperation;
    private final PostDetailAssembler detailAssembler;

    @GetMapping("/posts/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> like(@PathVariable int id) {
        return repository.findById(id)
                .map(likeOperation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/posts/{id}/rating")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Double> rating(@PathVariable int id,
                                       @RequestParam int star) {
        return repository.findById(id)
                .map(post -> Pair.of(post, star))
                .map(starOperation)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts/{id}/hide")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Link> hide(@PathVariable int id) {
        return repository.findById(id)
                .map(hideOperation)
                .map(PostLinks::linkToHidePost)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts/{id}/publish")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Link> publish(@PathVariable int id) {
        return repository.findById(id)
                .map(publishOperation)
                .map(PostLinks::linkToPublishPost)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Link> delete(@PathVariable int id) {
        return repository.findById(id)
                .map(deleteOperation)
                .map(PostLinks::linkToDeletePost)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @PostMapping("/posts")
    @PreAuthorize("isAuthenticated()")
    public PostDetailModel createPost(@Valid @RequestBody PostRequest postRequest) {
        return detailAssembler.toModel(createOperation.apply(postRequest));
    }
}
