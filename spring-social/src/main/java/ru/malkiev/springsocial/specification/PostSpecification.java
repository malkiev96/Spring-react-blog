package ru.malkiev.springsocial.specification;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import ru.malkiev.springsocial.entity.Category_;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.entity.Post.Status;
import ru.malkiev.springsocial.entity.Post_;
import ru.malkiev.springsocial.entity.Tag_;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Data
public class PostSpecification implements Supplier<Optional<Specification<Post>>> {

    private Integer userId;
    private Integer tagId;
    private List<Integer> catIds;
    private List<Status> statuses = Collections.singletonList(Status.PUBLISHED);

    @Override
    public Optional<Specification<Post>> get() {
        SpecificationBuilder<Post> specBuilder = new SpecificationBuilder<>();
        specBuilder.accept(emptySpecification);
        Optional.ofNullable(userId).ifPresent(p -> specBuilder.accept(byUserId(p)));
        Optional.ofNullable(tagId).ifPresent(p -> specBuilder.accept(byTagId(p)));
        Optional.ofNullable(catIds).ifPresent(p -> specBuilder.accept(byCatIds(p)));
        Optional.ofNullable(statuses).ifPresent(p -> specBuilder.accept(byStatuses(p)));
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

    private Specification<Post> byStatuses(List<Status> statuses) {
        return (root, query, cb) -> root.get(Post_.STATUS).in(statuses);
    }
}
