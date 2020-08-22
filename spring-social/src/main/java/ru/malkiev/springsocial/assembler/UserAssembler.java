package ru.malkiev.springsocial.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.model.UserModel;

import static ru.malkiev.springsocial.link.UserLinks.linkToPosts;
import static ru.malkiev.springsocial.link.UserLinks.linkToUser;

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
