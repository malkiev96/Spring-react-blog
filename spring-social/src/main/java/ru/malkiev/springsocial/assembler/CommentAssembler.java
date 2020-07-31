package ru.malkiev.springsocial.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.Comment;
import ru.malkiev.springsocial.model.CommentModel;
import ru.malkiev.springsocial.service.UserService;

import static ru.malkiev.springsocial.link.CommentLinks.*;

@Component
@AllArgsConstructor
public class CommentAssembler implements RepresentationModelAssembler<Comment, CommentModel> {

    private final UserService userService;

    @Override
    public @NotNull CommentModel toModel(@NotNull Comment entity) {
        CommentModel model = new CommentModel(entity);
        model.setChilds(toCollectionModel(entity.getChilds()));

        model.add(linkToComment(entity).withSelfRel());
        model.addIf(canDeleteComment(entity), () -> linkToDelete(entity));
        model.addIf(canReplyComment(), () -> linkToReply(entity));

        return model;
    }

    private boolean canDeleteComment(Comment entity) {
        return userService.getCurrentUser().map(entity::canEdit).orElse(false);
    }

    private boolean canReplyComment() {
        return userService.getCurrentUser().isPresent();
    }

}
