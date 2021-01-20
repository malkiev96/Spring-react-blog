package ru.malkiev.blog.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import ru.malkiev.blog.util.SpecificationBuilder;

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
    private List<Integer> tagIds;
    private List<PostStatus> statuses = singletonList(PostStatus.PUBLISHED);

    @Override
    public Optional<Specification<Post>> get() {
        SpecificationBuilder<Post> specificationBuilder = new SpecificationBuilder<>();
        specificationBuilder.accept(emptySpecification);
        Optional.ofNullable(userId).ifPresent(p -> specificationBuilder.accept(byUserId(p)));
        Optional.ofNullable(tagId).ifPresent(p -> specificationBuilder.accept(byTagId(p)));
        Optional.ofNullable(catIds).ifPresent(p -> specificationBuilder.accept(byCatIds(p)));
        Optional.ofNullable(tagIds).ifPresent(p -> specificationBuilder.accept(byTagIds(p)));
        Optional.ofNullable(statuses).ifPresent(p -> specificationBuilder.accept(byStatuses(p)));
        Optional.ofNullable(fromView).ifPresent(p -> specificationBuilder.accept(byViewMore(p)));
        return specificationBuilder.build();
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

    private Specification<Post> byTagIds(List<Integer> ids) {
        Specification<Post> spec = (root, query, cb) -> root.join(Post_.TAGS).get(Tag_.ID).in(ids);
        return spec.and(distinct());
    }

    private Specification<Post> distinct() {
        return (root, query, cb) -> {
            query.distinct(true);
            return null;
        };
    }

    private Specification<Post> byStatuses(List<PostStatus> statuses) {
        return (root, query, cb) -> root.get(Post_.STATUS).in(statuses);
    }

    private Specification<Post> byViewMore(int fromView) {
        return (root, query, cb) -> cb.greaterThan(root.get(Post_.VIEW_COUNT), fromView);
    }
}
