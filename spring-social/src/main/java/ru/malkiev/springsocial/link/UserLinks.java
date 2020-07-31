package ru.malkiev.springsocial.link;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import ru.malkiev.springsocial.controller.PostController;
import ru.malkiev.springsocial.controller.UserController;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.specification.PostSpecification;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserLinks {

    public static Link linkToUser(User entity) {
        return linkTo(methodOn(UserController.class).getOne(entity.getId())).withRel("user");
    }

    public static Link linkToPosts(User entity) {
        PostSpecification specification = new PostSpecification();
        specification.setUserId(entity.getId());
        return linkTo(methodOn(PostController.class).getPosts(specification, Pageable.unpaged()))
                .withRel("posts")
                .withType("GET");
    }

    public static Link linkToEditUser(User entity) {
        return linkTo(methodOn(UserController.class).getOne(entity.getId()))
                .withRel("edit")
                .withType("POST");
    }
}
