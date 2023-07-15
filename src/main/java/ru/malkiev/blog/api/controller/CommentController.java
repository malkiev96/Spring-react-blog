package ru.malkiev.blog.api.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.api.mapper.CommentMapper;
import ru.malkiev.blog.api.model.CommentModel;
import ru.malkiev.blog.application.exception.CommentNotFoundException;
import ru.malkiev.blog.application.exception.PostNotFoundException;
import ru.malkiev.blog.application.operation.CreateComment;
import ru.malkiev.blog.application.operation.DeleteComment;
import ru.malkiev.blog.application.operation.ReplyComment;
import ru.malkiev.blog.application.security.CustomUserDetails;
import ru.malkiev.blog.domain.entity.Post;
import ru.malkiev.blog.domain.repository.CommentRepository;
import ru.malkiev.blog.domain.repository.PostRepository;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentRepository repository;
  private final CreateComment createComment;
  private final ReplyComment replyComment;
  private final DeleteComment deleteComment;
  private final PostRepository postRepository;
  private final CommentMapper commentMapper;

  @GetMapping("/comments/{id}")
  public ResponseEntity<CommentModel> getOne(@PathVariable int id) {
    return repository.findById(id)
        .map(commentMapper::toModel)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new CommentNotFoundException(id));
  }

  @GetMapping("/comments/post/{id}")
  public List<CommentModel> getAllOfPosts(@PathVariable int id) {
    return postRepository.findById(id)
        .map(post -> repository.findAllByPostAndParentIsNull(post,
            Sort.by("createdDate").descending()))
        .map(commentMapper::toCollectionModel)
        .orElseThrow(() -> new CommentNotFoundException(id));
  }

  @PostMapping("/comments/post/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CommentModel> create(@PathVariable int id, @RequestBody String message) {
    return postRepository.findById(id)
        .map(post -> Pair.of(post, message))
        .map(createComment)
        .map(repository::save)
        .map(commentMapper::toModel)
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
        .map(replyComment)
        .map(repository::save)
        .map(commentMapper::toModel)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new CommentNotFoundException(parentId));
  }

  @DeleteMapping("/comments/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CommentModel> delete(@PathVariable int id,
      @AuthenticationPrincipal CustomUserDetails principal) {
    return repository.findById(id)
//                .filter(comment -> comment.canEdit(principal.getUser()))
        .map(deleteComment)
        .map(repository::save)
        .map(commentMapper::toModel)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new CommentNotFoundException(id));
  }
}
