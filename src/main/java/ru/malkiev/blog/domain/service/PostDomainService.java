package ru.malkiev.blog.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.malkiev.blog.domain.entity.Post;
import ru.malkiev.blog.domain.entity.User;
import ru.malkiev.blog.domain.model.PostStatus;

@Service
public class PostDomainService {

  private final Map<PostStatus, PostOperationPredicate> predicates = new HashMap<>();

  @Autowired
  public PostDomainService(List<PostOperationPredicate> predicates) {
    predicates.forEach(pop -> this.predicates.put(pop.getTargetStatus(), pop));
  }

  public Post setStatus(PostStatus newStatus, Post post, User currentUser) {
    PostOperationPredicate predicate = Optional.ofNullable(predicates.get(newStatus))
        .orElseThrow(() -> new IllegalArgumentException("Predicate not found for " + newStatus));
    if (predicate.test(post, currentUser)) {
      post.setStatus(newStatus);
      return post;
    } else {
      throw new IllegalStateException("Operation not allowed");
    }

  }

}
