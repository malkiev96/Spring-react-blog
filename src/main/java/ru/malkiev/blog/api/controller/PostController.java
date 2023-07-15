package ru.malkiev.blog.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.api.mapper.PostMapper;
import ru.malkiev.blog.api.model.PostDetailModel;
import ru.malkiev.blog.api.model.PostModel;
import ru.malkiev.blog.application.exception.PostNotFoundException;
import ru.malkiev.blog.domain.entity.Post;
import ru.malkiev.blog.domain.model.PostSpecification;
import ru.malkiev.blog.domain.repository.PostRepository;

@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostRepository repository;
  private final PostMapper mapper;

  @GetMapping("/posts/{id}")
  public ResponseEntity<PostDetailModel> getOne(@PathVariable int id) {
    return repository.findById(id)
        .map(this::incrementView)
        .map(repository::save)
        .map(mapper::toDetailModel)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new PostNotFoundException(id));
  }

  @GetMapping("/posts")
  public ResponseEntity<Page<PostModel>> getPosts(PostSpecification spec,
      @PageableDefault Pageable pageable) {
    Page<Post> posts = repository.findAll(spec.get(), pageable);
    return ResponseEntity.ok(mapper.toPagedModel(posts));
  }

  private Post incrementView(Post post) {
    post.setViewCount(post.getViewCount() + 1);
    return post;
  }
}
