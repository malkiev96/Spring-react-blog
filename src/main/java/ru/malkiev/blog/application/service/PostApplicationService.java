package ru.malkiev.blog.application.service;

import static ru.malkiev.blog.domain.model.PostStatus.CREATED;
import static ru.malkiev.blog.domain.model.PostStatus.PUBLISHED;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.malkiev.blog.api.dto.PostDto;
import ru.malkiev.blog.application.exception.CategoryNotFoundException;
import ru.malkiev.blog.application.exception.PostNotFoundException;
import ru.malkiev.blog.domain.entity.Category;
import ru.malkiev.blog.domain.entity.Post;
import ru.malkiev.blog.domain.entity.User;
import ru.malkiev.blog.domain.model.PostStatus;
import ru.malkiev.blog.domain.repository.CategoryRepository;
import ru.malkiev.blog.domain.repository.PostRepository;
import ru.malkiev.blog.domain.repository.TagRepository;
import ru.malkiev.blog.domain.service.PostDomainService;

@Service
@Transactional
@AllArgsConstructor
public class PostApplicationService {

  private final PostRepository repository;
  private final CategoryRepository categoryRepository;
  private final TagRepository tagRepository;
  private final DocumentService documentService;
  private final PostDomainService domainService;
  private final CurrentUser currentUser;

  public Post hidePost(@NonNull Integer id) {
    User user = currentUser.getUser();
    Post post = repository.findByIdForUpdate(id)
        .orElseThrow(() -> new PostNotFoundException(id));
    return domainService.setStatus(PostStatus.CREATED, post, user);
  }

  public Post publishPost(@NonNull Integer id) {
    User user = currentUser.getUser();
    Post post = repository.findByIdForUpdate(id)
        .orElseThrow(() -> new PostNotFoundException(id));
    return domainService.setStatus(PostStatus.PUBLISHED, post, user);
  }

  public Post deletePost(@NonNull Integer id) {
    User user = currentUser.getUser();
    Post post = repository.findByIdForUpdate(id)
        .orElseThrow(() -> new PostNotFoundException(id));
    return domainService.setStatus(PostStatus.DELETED, post, user);
  }

  public Post blockPost(@NonNull Integer id) {
    User user = currentUser.getUser();
    Post post = repository.findByIdForUpdate(id)
        .orElseThrow(() -> new PostNotFoundException(id));
    return domainService.setStatus(PostStatus.BLOCKED, post, user);
  }

  public Post createPost(@NonNull PostDto dto) {
    Post post = Optional.ofNullable(dto.getId())
        .flatMap(repository::findByIdForUpdate)
        .orElseGet(Post::new);

    post.setTitle(dto.getTitle());
    post.setDescription(dto.getDescription());
    post.setText(dto.getText());
    post.setStatus(dto.isPosted() ? PUBLISHED : CREATED);

    String previewId = dto.getPreviewId();
    post.setPreview(previewId == null ? null : documentService.createDocumentByFileId(previewId));

    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
    post.setCategory(category);

    post.setDocuments(documentService.createDocumentList(dto.getDocumentIds()));
    post.setTags(tagRepository.findAllById(dto.getTagIds()));

    return repository.save(post);
  }


}
