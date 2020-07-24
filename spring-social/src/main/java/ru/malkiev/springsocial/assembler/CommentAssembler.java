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
        if (entity.getChilds() != null) {
            model.setChilds(toCollectionModel(entity.getChilds()));
        }

        User currentUser = getCurrentUser();

        model.add(createLinkToSelf(entity));
        model.addIf(entity.canEdit(currentUser), createDeleteLink(entity));
        model.addIf(currentUser != null, createReplyLink(entity));

        return model;
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUser();
        }
        return null;
    }

    private Link createLinkToSelf(Comment entity) {
        return linkTo(methodOn(CommentController.class).getOne(entity.getId())).withSelfRel();
    }

    private Supplier<Link> createDeleteLink(Comment entity) {
        return () -> linkTo(methodOn(CommentController.class).delete(entity.getId(), null))
                .withRel("delete")
                .withType("DELETE");
    }

    private Supplier<Link> createReplyLink(Comment entity) {
        return () -> linkTo(methodOn(CommentController.class)
                .reply(entity.getPost().getId(), entity.getId(), null)).withRel("reply")
                .withType("POST");
    }
}
