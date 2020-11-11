package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.PostDetailAssembler;
import ru.malkiev.blog.exception.PostNotFoundException;
import ru.malkiev.blog.link.PostLinks;
import ru.malkiev.blog.model.PostDetailModel;
import ru.malkiev.blog.model.payload.PostDto;
import ru.malkiev.blog.operation.*;
import ru.malkiev.blog.repository.PostRepository;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
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
    public ResponseEntity<Boolean> like(@PathVariable int id) {
        return repository.findById(id)
                .map(likeOperation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/posts/{id}/rating")
    public ResponseEntity<Double> rating(@PathVariable int id,
                                       @RequestParam int star) {
        return repository.findById(id)
                .map(post -> Pair.of(post, star))
                .map(starOperation)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts/{id}/hide")
    public ResponseEntity<Link> hide(@PathVariable int id) {
        return repository.findById(id)
                .map(hideOperation)
                .map(repository::save)
                .map(PostLinks::linkToHidePost)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts/{id}/publish")
    public ResponseEntity<Link> publish(@PathVariable int id) {
        return repository.findById(id)
                .map(publishOperation)
                .map(repository::save)
                .map(PostLinks::linkToPublishPost)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts/{id}/delete")
    public ResponseEntity<Link> delete(@PathVariable int id) {
        return repository.findById(id)
                .map(deleteOperation)
                .map(repository::save)
                .map(PostLinks::linkToDeletePost)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @PostMapping("/posts")
    public PostDetailModel createPost(@Valid @RequestBody PostDto postDto) {
        return detailAssembler.toModel(
                repository.save(
                        createOperation.apply(postDto)
                )
        );
    }
}
