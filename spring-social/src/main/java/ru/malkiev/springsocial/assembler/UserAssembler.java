package ru.malkiev.springsocial.assembler;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.model.UserModel;

@Component
public class UserAssembler implements RepresentationModelAssembler<User, UserModel> {

    @Override
    public UserModel toModel(User entity) {
        UserModel model = new UserModel(entity);
        return model;
    }
}
