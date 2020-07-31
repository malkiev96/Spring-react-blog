package ru.malkiev.springsocial.link;

import org.springframework.hateoas.Link;
import ru.malkiev.springsocial.controller.CommentController;
import ru.malkiev.springsocial.controller.PostController;
import ru.malkiev.springsocial.entity.Post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class PostLinks {

    public static Link linkToDetailPost(Post entity) {
        return linkTo(methodOn(PostController.class).getOne(entity.getId())).withRel("detail");
    }

    public static Link linkToComments(Post entity) {
        return linkTo(methodOn(CommentController.class).getAllOfPosts(entity.getId())).withRel("comments");
    }

    public static Link linkToAddComment(Post entity) {
        return linkTo(methodOn(CommentController.class).create(entity.getId(), ""))
                .withRel("addComment")
                .withType("POST");
    }


}
