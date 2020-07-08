package ru.malkiev.springsocial.model;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.springsocial.entity.Role;
import ru.malkiev.springsocial.entity.User;

@Getter
public class UserModel extends RepresentationModel<UserModel> {

    private final int id;
    private final String name;
    private final String email;
    private final String imageUrl;
    private final boolean emailVerified;
    private final boolean isAdmin;

    public UserModel(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.emailVerified = user.getEmailVerified();
        this.isAdmin = user.getRole().equals(Role.ROLE_ADMIN);
    }
}
