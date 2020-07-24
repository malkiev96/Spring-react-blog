package ru.malkiev.springsocial.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.controller.CommentController;
import ru.malkiev.springsocial.controller.PostController;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.model.PostDetailModel;
import ru.malkiev.springsocial.security.UserPrincipal;

import java.util.function.Supplier;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@AllArgsConstructor
public class PostDetailAssembler implements RepresentationModelAssembler<Post, PostDetailModel> {

    private final ImageAssembler imageAssembler;

    @Override
    public @NotNull PostDetailModel toModel(@NotNull Post entity) {
        PostDetailModel model = new PostDetailModel(entity);
        if (entity.getImagePreview() != null) {
            model.setPreview(imageAssembler.toModel(entity.getImagePreview()));
        }
        if (entity.getImages() != null) {
            model.setImages(imageAssembler.toCollectionModel(entity.getImages()));
        }
        model.add(createLinkToSelf(entity));
        model.add(createGetCommentsLink(entity));
        model.addIf(getCurrentUser()!=null,createAddCommentLink(entity));
        return model;
    }

    private Link createLinkToSelf(Post entity) {
        return linkTo(methodOn(PostController.class).getOne(entity.getId())).withSelfRel();
    }

    private Link createGetCommentsLink(Post entity){
        return linkTo(methodOn(CommentController.class).getAllOfPosts(entity.getId()))
                .withRel("comments")
                .withType("GET");
    }

    private Supplier<Link> createAddCommentLink(Post entity) {
        return () -> linkTo(methodOn(CommentController.class).create(entity.getId(),null))
                .withRel("addComment")
                .withType("POST");
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUser();
        }
        return null;
    }

}
