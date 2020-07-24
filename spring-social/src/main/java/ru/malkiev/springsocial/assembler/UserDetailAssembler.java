package ru.malkiev.springsocial.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.controller.CommentController;
import ru.malkiev.springsocial.controller.PostController;
import ru.malkiev.springsocial.controller.UserController;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.model.UserDetailModel;
import ru.malkiev.springsocial.security.UserPrincipal;

import java.util.function.Supplier;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDetailAssembler implements RepresentationModelAssembler<User, UserDetailModel> {

    @Override
    public @NotNull UserDetailModel toModel(@NotNull User entity) {
        UserDetailModel model = new UserDetailModel(entity);

        model.add(createLinkToSelf(entity));
        model.add(createGetPostsLink(entity));
        model.addIf(allowEdit(entity),createEditLink(entity));

        return model;
    }

    private boolean allowEdit(User entity){
        return (entity.equals(getCurrentUser()));
    }

    private Link createLinkToSelf(User entity) {
        return linkTo(methodOn(UserController.class).getOne(entity.getId())).withSelfRel();
    }

    private Link createGetPostsLink(User entity){
        return linkTo(methodOn(PostController.class).getPostsByUser(entity.getId(), Pageable.unpaged()))
                .withRel("posts")
                .withType("GET");
    }

    private Supplier<Link> createEditLink(User entity) {
        return () -> linkTo(methodOn(UserController.class).getOne(entity.getId()))
                .withRel("edit")
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
