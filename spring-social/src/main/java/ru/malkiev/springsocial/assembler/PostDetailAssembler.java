package ru.malkiev.springsocial.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.model.PostDetailModel;
import ru.malkiev.springsocial.service.UserService;

import static ru.malkiev.springsocial.link.PostLinks.*;

@Component
@AllArgsConstructor
public class PostDetailAssembler implements RepresentationModelAssembler<Post, PostDetailModel> {

    private final UserService userService;

    @Override
    public @NotNull PostDetailModel toModel(@NotNull Post entity) {
        PostDetailModel model = new PostDetailModel(entity);

        model.add(linkToDetailPost(entity).withSelfRel());
        model.add(linkToComments(entity));
        model.addIf(canAddComment(), () -> linkToAddComment(entity));
        return model;
    }

    private boolean canAddComment() {
        return userService.getCurrentUser().isPresent();
    }
}
