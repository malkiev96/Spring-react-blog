package ru.malkiev.blog.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.model.UserModel;

import static ru.malkiev.blog.link.UserLinks.linkToPosts;
import static ru.malkiev.blog.link.UserLinks.linkToUser;

@Component
public class UserAssembler implements RepresentationModelAssembler<User, UserModel> {

    @Override
    public @NotNull UserModel toModel(@NotNull User entity) {
        UserModel model = new UserModel(entity);

        model.add(linkToUser(entity));
        model.add(linkToPosts(entity));

        return model;
    }
}
