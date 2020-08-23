package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.Role;
import ru.malkiev.blog.entity.User;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class UserDetailModel extends RepresentationModel<UserDetailModel> {

    private int id;
    private String name;
    private String email;
    private String imageUrl;
    private boolean emailVerified;
    private boolean isAdmin;
    private String city;
    private String about;
    private LocalDate birthDate;

    public UserDetailModel(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.emailVerified = user.getEmailVerified();
        this.isAdmin = user.getRole().equals(Role.ROLE_ADMIN);
        this.city = user.getCity();
        this.about = user.getAbout();
        this.birthDate = user.getBirthDate();
    }
}
