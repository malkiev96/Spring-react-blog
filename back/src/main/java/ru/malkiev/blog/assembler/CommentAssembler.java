package ru.malkiev.blog.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Comment;
import ru.malkiev.blog.model.CommentModel;
import ru.malkiev.blog.service.UserService;

import static ru.malkiev.blog.link.CommentLinks.*;

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
