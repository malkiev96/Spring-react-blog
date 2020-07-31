package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.springsocial.assembler.PostAssembler;
import ru.malkiev.springsocial.assembler.PostDetailAssembler;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.exception.PostNotFoundException;
import ru.malkiev.springsocial.model.PostDetailModel;
import ru.malkiev.springsocial.model.PostModel;
import ru.malkiev.springsocial.model.payload.PostRequest;
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

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailModel> getOne(@PathVariable int id) {
        return postService.findById(id)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDetailModel> delete(@PathVariable int id,
                                                  @CurrentUser UserPrincipal principal) {
        User user = principal.getUser();
        return postService.findById(id)
                .filter(post -> user.isAdmin() || post.getCreatedBy().equals(user))
                .map(postService::delete)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/posts/{id}/hide")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDetailModel> hide(@PathVariable int id,
                                                @CurrentUser UserPrincipal principal) {
        User user = principal.getUser();
        return postService.findById(id)
                .filter(post -> user.isAdmin() || post.getCreatedBy().equals(user))
                .map(postService::hide)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/posts/{id}/publish")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDetailModel> publish(@PathVariable int id,
                                                   @CurrentUser UserPrincipal principal) {
        User user = principal.getUser();
        return postService.findById(id)
                .filter(post -> user.isAdmin() || post.getCreatedBy().equals(user))
                .map(postService::publish)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/posts")
    public ResponseEntity<PagedModel<PostModel>> getPosts(PostSpecification spec,
                                                          @PageableDefault Pageable pageable) {
        return spec.get()
                .map(s -> postService.findAll(s, pageable))
                .map(postAssembler::toPagedModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("posts")
    @PreAuthorize("isAuthenticated()")
    public PostDetailModel createPost(@Valid @RequestBody PostRequest postRequest) {
        return detailAssembler.toModel(postService.create(postRequest));
    }
}
