package ru.malkiev.blog.link;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import ru.malkiev.blog.controller.PostController;
import ru.malkiev.blog.controller.UserController;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.spec.PostSpec;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLinks {

    public static Link linkToUser(User entity) {
        return linkTo(methodOn(UserController.class).getOne(entity.getId())).withRel("user");
    }

    public static Link linkToPosts(User entity) {
        PostSpec specification = new PostSpec();
        specification.setUserId(entity.getId());
        return linkTo(methodOn(PostController.class).getPosts(specification, Pageable.unpaged()))
                .withRel("posts");
    }

    public static Link linkToEditUser(User entity) {
        return linkTo(methodOn(UserController.class).edit(entity.getId(), null))
                .withRel("edit")
                .withType("POST");
    }
}
