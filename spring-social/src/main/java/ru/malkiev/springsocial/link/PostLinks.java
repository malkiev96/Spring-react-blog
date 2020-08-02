package ru.malkiev.springsocial.link;

import org.springframework.hateoas.Link;
import ru.malkiev.springsocial.controller.CommentController;
import ru.malkiev.springsocial.controller.PostController;
import ru.malkiev.springsocial.controller.PostOperationController;
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

    public static Link linkToHidePost(Post entity) {
        return linkTo(methodOn(PostOperationController.class).hide(entity.getId())).withRel("hide");
    }

    public static Link linkToPublishPost(Post entity) {
        return linkTo(methodOn(PostOperationController.class).publish(entity.getId())).withRel("publish");
    }

    public static Link linkToDeletePost(Post entity) {
        return linkTo(methodOn(PostOperationController.class).delete(entity.getId())).withRel("delete");
    }

    public static Link linkToLikePost(Post entity) {
        return linkTo(methodOn(PostOperationController.class).like(entity.getId())).withRel("like");
    }

    public static Link linkToAddRatingPost(Post entity) {
        return linkTo(methodOn(PostOperationController.class).rating(entity.getId(),5)).withRel("star");
    }
}
