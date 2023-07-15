package ru.malkiev.blog.api.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.api.dto.PostDto;
import ru.malkiev.blog.api.mapper.PostMapper;
import ru.malkiev.blog.api.model.PostDetailModel;
import ru.malkiev.blog.api.model.PostModel;
import ru.malkiev.blog.application.service.PostApplicationService;
import ru.malkiev.blog.domain.entity.Post;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PostOperationController {

  private final PostApplicationService service;
  private final PostMapper mapper;

  @PostMapping("/posts/{id}/hide")
  public ResponseEntity<PostModel> hide(@PathVariable int id) {
    Post post = service.hidePost(id);
    return ResponseEntity.ok(mapper.toModel(post));
  }

  @PostMapping("/posts/{id}/publish")
  public ResponseEntity<PostModel> publish(@PathVariable int id) {
    Post post = service.publishPost(id);
    return ResponseEntity.ok(mapper.toModel(post));
  }

  @PostMapping("/posts/{id}/delete")
  public ResponseEntity<PostModel> delete(@PathVariable int id) {
    Post post = service.deletePost(id);
    return ResponseEntity.ok(mapper.toModel(post));
  }

  @PostMapping("/posts/{id}/block")
  public ResponseEntity<PostModel> block(@PathVariable int id) {
    Post post = service.blockPost(id);
    return ResponseEntity.ok(mapper.toModel(post));
  }

  @PostMapping("/posts")
  public ResponseEntity<PostDetailModel> createPost(@Valid @RequestBody PostDto postDto) {
    Post post = service.createPost(postDto);
    return ResponseEntity.ok(mapper.toDetailModel(post));
  }

}
