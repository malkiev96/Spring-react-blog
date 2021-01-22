package ru.malkiev.blog.link;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import ru.malkiev.blog.controller.CommentController;
import ru.malkiev.blog.controller.PostController;
import ru.malkiev.blog.controller.PostOperationController;
import ru.malkiev.blog.entity.Post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static Link linkToEditPost(Post entity) {
        return linkTo(methodOn(PostOperationController.class).createPost(null))
                .withRel("edit")
                .withType("POST");
    }

    public static Link linkToAddRatingPost(Post entity) {
        return linkTo(methodOn(PostOperationController.class).rating(entity.getId(), 5)).withRel("star");
    }

    public static Link linkToDeleteRatingPost(Post entity) {
        return linkTo(methodOn(PostOperationController.class).deleteStarOfUser(entity.getId())).withRel("deleteStar");
    }
}
