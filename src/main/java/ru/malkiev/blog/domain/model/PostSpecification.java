package ru.malkiev.blog.domain.model;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import ru.malkiev.blog.domain.entity.Post;
import ru.malkiev.blog.domain.entity.Post_;
import ru.malkiev.blog.domain.entity.Tag_;

@Data
public class PostSpecification implements Supplier<Specification<Post>> {

  private Integer userId;
  private Integer tagId;
  private Integer fromView;
  private List<Integer> catIds;
  private List<String> tagCodes;
  private List<PostStatus> statuses = singletonList(PostStatus.PUBLISHED);

  @Override
  public Specification<Post> get() {
    List<Specification<Post>> specs = new ArrayList<>();

    Optional.ofNullable(userId).map(this::byUserId).ifPresent(specs::add);
    Optional.ofNullable(tagId).map(this::byTagId).ifPresent(specs::add);
    Optional.ofNullable(catIds).map(this::byCatIds).ifPresent(specs::add);
    Optional.ofNullable(tagCodes).map(this::byTagCodes).ifPresent(specs::add);
    Optional.ofNullable(statuses).map(this::byStatuses).ifPresent(specs::add);
    Optional.ofNullable(fromView).map(this::byViewMore).ifPresent(specs::add);

    Specification<Post> spec = empty;
    for (Specification<Post> s : specs) {
      spec = spec != null ? spec.and(s) : null;
    }
    return spec;
  }

  private Specification<Post> empty = (root, query, cb) -> cb.and();

  private Specification<Post> byUserId(int id) {
    return (root, query, cb) -> cb.equal(root.get(Post_.createdBy), id);
  }

  private Specification<Post> byTagId(int id) {
    return (root, query, cb) -> cb.equal(root.join(Post_.tags).get(Tag_.id), id);
  }

  private Specification<Post> byCatIds(List<Integer> ids) {
    return (root, query, cb) -> root.get(Post_.category).in(ids);
  }

  private Specification<Post> byTagCodes(List<String> codes) {
    return (root, query, cb) -> {
      query.distinct(true);
      return root.join(Post_.tags).get(Tag_.code).in(codes);
    };
  }

  private Specification<Post> byStatuses(List<PostStatus> statuses) {
    return (root, query, cb) -> root.get(Post_.status).in(statuses);
  }

  private Specification<Post> byViewMore(int fromView) {
    return (root, query, cb) -> cb.greaterThan(root.get(Post_.viewCount), fromView);
  }
}
