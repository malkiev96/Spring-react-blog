package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.User;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class UserModel extends RepresentationModel<UserModel> {

    private int id;
    private String name;
    private String email;
    private String imageUrl;

    public UserModel(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
    }
}
