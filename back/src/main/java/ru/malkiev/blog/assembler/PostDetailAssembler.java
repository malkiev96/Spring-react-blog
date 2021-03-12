package ru.malkiev.blog.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.link.PostLinks;
import ru.malkiev.blog.model.PostDetailModel;
import ru.malkiev.blog.repository.PostLikeRepository;
import ru.malkiev.blog.repository.PostRatingRepository;
import ru.malkiev.blog.service.UserService;

import static ru.malkiev.blog.assembler.PostAssembler.*;
import static ru.malkiev.blog.link.PostLinks.*;
import static ru.malkiev.blog.link.UserLinks.linkToUser;

@Component
@AllArgsConstructor
public class PostDetailAssembler implements RepresentationModelAssembler<Post, PostDetailModel> {

    private final UserService userService;
    private final PostLikeRepository likeRepository;
    private final PostRatingRepository ratingRepository;
    private final DocumentAssembler documentAssembler;
    private final TagModelAssembler tagModelAssembler;

    @Override
    public @NotNull PostDetailModel toModel(@NotNull Post entity) {
        PostDetailModel model = new PostDetailModel(entity);
        model.setRating(ratingRepository.getAvgRatingByPost(entity));
        model.setLikedCount(likeRepository.countByPost(entity));
        model.setDocuments(documentAssembler.toCollectionModel(entity.getDocuments()));
        model.setTags(tagModelAssembler.toCollectionModel(entity.getTags()));

        model.add(linkToDetailPost(entity).withSelfRel());
        model.add(linkToComments(entity));
        model.add(linkToUser(entity.getCreatedBy()).withRel("createdBy"));

        userService.getCurrentUser().ifPresent(user -> {
            model.setLiked(likeRepository.findByCreatedByAndPost(user, entity).isPresent());
            ratingRepository.findByCreatedByAndPost(user, entity)
                    .ifPresent(rating -> {
                        model.setMyStar(rating.getStar());
                        model.add(linkToDeleteRatingPost(entity));
                    });
            model.add(linkToAddComment(entity));
            model.add(linkToLikePost(entity));
            model.add(linkToAddRatingPost(entity));
            model.addIf(canDeletePost(entity, user), PostLinks::linkToEditPost);
            model.addIf(canHidePost(entity, user), () -> linkToHidePost(entity));
            model.addIf(canPublishPost(entity, user), () -> linkToPublishPost(entity));
            model.addIf(canDeletePost(entity, user), () -> linkToDeletePost(entity));
        });

        return model;
    }
}
