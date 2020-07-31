package ru.malkiev.springsocial.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.model.UserDetailModel;
import ru.malkiev.springsocial.service.UserService;

import static ru.malkiev.springsocial.link.UserLinks.*;

@Component
@AllArgsConstructor
public class UserDetailAssembler implements RepresentationModelAssembler<User, UserDetailModel> {

    private final UserService userService;

    @Override
    public @NotNull UserDetailModel toModel(@NotNull User entity) {
        UserDetailModel model = new UserDetailModel(entity);

        model.add(linkToUser(entity));
        model.add(linkToPosts(entity));
        model.addIf(canEditProfile(entity), () -> linkToEditUser(entity));

        return model;
    }

    private boolean canEditProfile(User entity) {
        return userService.getCurrentUser().map(entity::equals).orElse(false);
    }
}
