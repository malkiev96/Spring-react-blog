package ru.malkiev.blog.specification;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import ru.malkiev.blog.entity.*;
import ru.malkiev.blog.entity.Post.Status;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Collections.singletonList;

@Data
public class PostSpecification implements Supplier<Optional<Specification<Post>>> {

    private Integer userId;
    private Integer tagId;
    private Integer fromView;
    private Boolean liked;
    private List<Integer> catIds;
    private List<String> tagNames;
    private List<Status> statuses = singletonList(Status.PUBLISHED);

    @Override
    public Optional<Specification<Post>> get() {
        SpecificationBuilder<Post> specBuilder = new SpecificationBuilder<>();
        specBuilder.accept(emptySpecification);
        Optional.ofNullable(userId).ifPresent(p -> specBuilder.accept(byUserId(p)));
        Optional.ofNullable(tagId).ifPresent(p -> specBuilder.accept(byTagId(p)));
        Optional.ofNullable(catIds).ifPresent(p -> specBuilder.accept(byCatIds(p)));
        Optional.ofNullable(tagNames).ifPresent(p -> specBuilder.accept(byTagNames(p)));
        Optional.ofNullable(statuses).ifPresent(p -> specBuilder.accept(byStatuses(p)));
        Optional.ofNullable(fromView).ifPresent(p -> specBuilder.accept(byViewMore(p)));
        if (userId != null && liked != null) specBuilder.accept(byLiked(userId));
        return specBuilder.build();
    }

    private Specification<Post> emptySpecification = (root, query, cb) -> cb.and();

    private Specification<Post> byUserId(int id) {
        return (root, query, cb) -> cb.equal(root.get(Post_.CREATED_BY), id);
    }

    private Specification<Post> byTagId(int id) {
        return (root, query, cb) -> cb.equal(root.join(Post_.TAGS).get(Tag_.ID), id);
    }

    private Specification<Post> byCatIds(List<Integer> ids) {
        return (root, query, cb) -> root.join(Post_.CATEGORY).get(Category_.ID).in(ids);
    }

    private Specification<Post> byTagNames(List<String> names) {
        Specification<Post> spec = (root, query, cb) -> root.join(Post_.TAGS).get(Tag_.DESCRIPTION).in(names);
        return spec.and(distinct());
    }

    private Specification<Post> distinct() {
        return (root, query, cb) -> {
            query.distinct(true);
            return null;
        };
    }

    private Specification<Post> byStatuses(List<Status> statuses) {
        return (root, query, cb) -> root.get(Post_.STATUS).in(statuses);
    }

    private Specification<Post> byViewMore(int fromView) {
        return (root, query, cb) -> cb.greaterThan(root.get(Post_.VIEW_COUNT), fromView);
    }

    private Specification<Post> byLiked(int userId) {
        return (root, query, cb) -> cb.equal(root.join(Post_.POST_LIKES).get(PostLike_.CREATED_BY), userId);
    }
}
