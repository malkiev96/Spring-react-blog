package ru.malkiev.springsocial.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.model.PostDetailModel;
import ru.malkiev.springsocial.repository.PostLikeRepository;
import ru.malkiev.springsocial.repository.PostRatingRepository;
import ru.malkiev.springsocial.service.UserService;

import static ru.malkiev.springsocial.assembler.PostAssembler.*;
import static ru.malkiev.springsocial.link.PostLinks.*;
import static ru.malkiev.springsocial.link.UserLinks.linkToUser;

@Component
@AllArgsConstructor
public class PostDetailAssembler implements RepresentationModelAssembler<Post, PostDetailModel> {

    private final UserService userService;
    private final PostLikeRepository likeRepository;
    private final PostRatingRepository ratingRepository;

    @Override
    public @NotNull PostDetailModel toModel(@NotNull Post entity) {
        PostDetailModel model = new PostDetailModel(entity);
        model.setRating(ratingRepository.getAvgRatingByPost(entity));

        model.add(linkToDetailPost(entity).withSelfRel());
        model.add(linkToComments(entity));
        model.add(linkToUser(entity.getCreatedBy()).withRel("createdBy"));

        userService.getCurrentUser().ifPresent(user -> {
            model.setLiked(likeRepository.findByCreatedByAndPost(user, entity).isPresent());
            ratingRepository.findByCreatedByAndPost(user, entity)
                    .ifPresent(rating -> model.setMyStar(rating.getStar()));
            model.add(linkToAddComment(entity));
            model.addIf(canDeletePost(entity, user), () -> linkToEditPost(entity));
            model.addIf(canHidePost(entity, user), () -> linkToHidePost(entity));
            model.addIf(canPublishPost(entity, user), () -> linkToPublishPost(entity));
            model.addIf(canDeletePost(entity, user), () -> linkToDeletePost(entity));
        });

        return model;
    }
}
