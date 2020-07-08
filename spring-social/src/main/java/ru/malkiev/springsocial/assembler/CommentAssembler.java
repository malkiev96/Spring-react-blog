package ru.malkiev.springsocial.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.controller.CommentController;
import ru.malkiev.springsocial.entity.Comment;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.model.CommentModel;
import ru.malkiev.springsocial.security.UserPrincipal;

import java.util.function.Supplier;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CommentAssembler implements RepresentationModelAssembler<Comment, CommentModel> {

    @Override
    public @NotNull CommentModel toModel(@NotNull Comment entity) {
        CommentModel model = new CommentModel(entity);
        model.setChilds(toCollectionModel(entity.getChilds()));

        model.add(createLinkToSelf(entity));
        model.addIf(entity.canEdit(getCurrentUser()), createDeleteLink(entity));

        return model;
    }

    private Link createLinkToSelf(Comment entity) {
        return linkTo(methodOn(CommentController.class).getOne(entity.getId())).withSelfRel();
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUser();
        }
        return null;
    }

    private Supplier<Link> createDeleteLink(Comment entity) {
        return () -> linkTo(methodOn(CommentController.class).deleteComment(entity.getId(), null))
                .withRel("delete")
                .withType("DELETE");
    }

}
