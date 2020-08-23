package ru.malkiev.blog.link;

import org.springframework.hateoas.Link;
import ru.malkiev.blog.controller.CommentController;
import ru.malkiev.blog.entity.Comment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CommentLinks {

    public static Link linkToComment(Comment entity) {
        return linkTo(methodOn(CommentController.class).getOne(entity.getId())).withSelfRel();
    }

    public static Link linkToDelete(Comment entity) {
        return linkTo(methodOn(CommentController.class).delete(entity.getId(), null))
                .withRel("delete")
                .withType("DELETE");
    }

    public static Link linkToReply(Comment entity) {
        return linkTo(methodOn(CommentController.class)
                .reply(entity.getPost().getId(), entity.getId(), null)).withRel("reply")
                .withType("POST");
    }
}
