package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.PostDetailAssembler;
import ru.malkiev.blog.dto.PostDto;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.exception.PostNotFoundException;
import ru.malkiev.blog.link.PostLinks;
import ru.malkiev.blog.model.PostDetailModel;
import ru.malkiev.blog.operation.*;
import ru.malkiev.blog.repository.PostRepository;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PostOperationController {

    private final PostRepository repository;
    private final DeletePost deletePost;
    private final HidePost hidePost;
    private final PublishPost publishPost;
    private final AddLike addLike;
    private final AddStar addStar;
    private final DeleteStar deleteStar;
    private final CreatePost createPost;
    private final PostDetailAssembler detailAssembler;

    @GetMapping("/posts/{id}/like")
    public ResponseEntity<Boolean> like(@PathVariable int id) {
        return repository.findById(id)
                .map(addLike)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/posts/{id}/rating")
    public ResponseEntity<Double> rating(@PathVariable int id,
                                         @RequestParam int star) {
        return repository.findById(id)
                .map(post -> Pair.of(post, star))
                .map(addStar)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @DeleteMapping("/posts/{id}/rating")
    public ResponseEntity<Double> deleteStarOfUser(@PathVariable int id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return ResponseEntity.ok(deleteStar.apply(post));
    }

    @GetMapping("/posts/{id}/hide")
    public ResponseEntity<Link> hide(@PathVariable int id) {
        return repository.findById(id)
                .map(hidePost)
                .map(repository::save)
                .map(PostLinks::linkToHidePost)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts/{id}/publish")
    public ResponseEntity<Link> publish(@PathVariable int id) {
        return repository.findById(id)
                .map(publishPost)
                .map(repository::save)
                .map(PostLinks::linkToPublishPost)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts/{id}/delete")
    public ResponseEntity<Link> delete(@PathVariable int id) {
        return repository.findById(id)
                .map(deletePost)
                .map(repository::save)
                .map(PostLinks::linkToDeletePost)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDetailModel> createPost(@Valid @RequestBody PostDto postDto) {
        return Optional.of(postDto)
                .map(createPost)
                .map(repository::save)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
