package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.springsocial.assembler.PostAssembler;
import ru.malkiev.springsocial.assembler.PostDetailAssembler;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.model.PostDetailModel;
import ru.malkiev.springsocial.model.PostModel;
import ru.malkiev.springsocial.model.payload.PostRequest;
import ru.malkiev.springsocial.repository.PostRepository;
import ru.malkiev.springsocial.security.CurrentUser;
import ru.malkiev.springsocial.security.UserPrincipal;
import ru.malkiev.springsocial.service.PostService;
import ru.malkiev.springsocial.specification.PostSpecification;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class PostController {

    private final PostAssembler postAssembler;
    private final PostDetailAssembler detailAssembler;
    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailModel> getOne(@PathVariable int id) {
        return postService.findById(id)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/posts")
    public ResponseEntity<PagedModel<PostModel>> getPosts(PostSpecification spec,
                                                          @PageableDefault Pageable pageable) {
        return spec.get()
                .map(s -> postRepository.findAll(s, pageable))
                .map(postAssembler::toPagedModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("posts")
    public PostDetailModel createPost(@Valid @RequestBody PostRequest postRequest, @CurrentUser UserPrincipal principal) {
        Post post = postService.create(postRequest, principal.getUser());
        return detailAssembler.toModel(post);
    }
}
