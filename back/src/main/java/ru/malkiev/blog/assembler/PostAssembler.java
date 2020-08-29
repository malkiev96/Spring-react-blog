package ru.malkiev.blog.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.model.PostModel;
import ru.malkiev.blog.repository.PostLikeRepository;
import ru.malkiev.blog.repository.PostRatingRepository;
import ru.malkiev.blog.service.UserService;

import static ru.malkiev.blog.link.PostLinks.*;
import static ru.malkiev.blog.link.UserLinks.linkToUser;


@Component
@AllArgsConstructor
public class PostAssembler implements RepresentationModelAssembler<Post, PostModel> {

    private final UserService userService;
    private final PostLikeRepository likeRepository;
    private final PagedResourcesAssembler<Post> pagedAssembler;

    public PagedModel<PostModel> toPagedModel(Page<Post> page) {
        return pagedAssembler.toModel(page, this);
    }

    @Override
    public @NotNull PostModel toModel(@NotNull Post entity) {
        PostModel model = new PostModel(entity);
        model.setLikedCount(likeRepository.countByPost(entity));

        model.add(linkToDetailPost(entity));
        model.add(linkToUser(entity.getCreatedBy()).withRel("createdBy"));
        userService.getCurrentUser().ifPresent(user -> {
            model.setLiked(likeRepository.findByCreatedByAndPost(user, entity).isPresent());
            model.add(linkToLikePost(entity));
            model.add(linkToAddRatingPost(entity));
            model.addIf(canHidePost(entity, user), () -> linkToHidePost(entity));
            model.addIf(canPublishPost(entity, user), () -> linkToPublishPost(entity));
            model.addIf(canDeletePost(entity, user), () -> linkToDeletePost(entity));
        });

        return model;
    }

    public static boolean canDeletePost(Post entity, User user) {
        return !entity.getStatus().equals(Post.Status.DELETED) &&
                (user.isAdmin() || user.equals(entity.getCreatedBy()));
    }

    public static boolean canHidePost(Post entity, User user) {
        return entity.getStatus().equals(Post.Status.PUBLISHED) &&
                (user.isAdmin() || user.equals(entity.getCreatedBy()));
    }

    public static boolean canPublishPost(Post entity, User user) {
        return entity.getStatus().equals(Post.Status.CREATED) &&
                (user.isAdmin() || user.equals(entity.getCreatedBy()));
    }

}
