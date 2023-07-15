package ru.malkiev.blog.domain.service;

import static ru.malkiev.blog.domain.model.PostStatus.BLOCKED;
import static ru.malkiev.blog.domain.model.PostStatus.CREATED;
import static ru.malkiev.blog.domain.model.PostStatus.DELETED;
import static ru.malkiev.blog.domain.model.PostStatus.PUBLISHED;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.domain.entity.Post;
import ru.malkiev.blog.domain.entity.User;
import ru.malkiev.blog.domain.model.PostStatus;

@RequiredArgsConstructor
public abstract class PostOperationPredicate implements BiPredicate<Post, User> {

  private final List<PostStatus> allowedStatuses;
  private final BiPredicate<Post, User> allowedUser;

  public abstract PostStatus getTargetStatus();

  @Override
  public boolean test(Post post, User user) {
    return allowedStatuses.contains(post.getStatus()) && allowedUser.test(post, user);
  }


  @Component
  public static class CanDeletePost extends PostOperationPredicate {

    public CanDeletePost() {
      super(Arrays.asList(CREATED, PUBLISHED), (post, user) -> post.getCreatedBy().equals(user));
    }

    @Override
    public PostStatus getTargetStatus() {
      return DELETED;
    }
  }

  @Component
  public static class CanPublishPost extends PostOperationPredicate {

    public CanPublishPost() {
      super(Arrays.asList(CREATED, DELETED), (post, user) -> post.getCreatedBy().equals(user));
    }

    @Override
    public PostStatus getTargetStatus() {
      return PUBLISHED;
    }
  }

  @Component
  public static class CanBlockPost extends PostOperationPredicate {

    public CanBlockPost() {
      super(Arrays.asList(CREATED, PUBLISHED, DELETED), (post, user) -> user.isAdmin());
    }

    @Override
    public PostStatus getTargetStatus() {
      return BLOCKED;
    }
  }

  @Component
  public static class CanHidePost extends PostOperationPredicate {

    public CanHidePost() {
      super(Arrays.asList(PUBLISHED), (post, user) -> user.isAdmin());
    }

    @Override
    public PostStatus getTargetStatus() {
      return CREATED;
    }
  }

}
