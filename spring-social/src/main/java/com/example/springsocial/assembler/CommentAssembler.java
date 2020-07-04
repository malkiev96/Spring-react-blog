package com.example.springsocial.assembler;

import com.example.springsocial.controller.CommentController;
import com.example.springsocial.entity.Comment;
import com.example.springsocial.model.CommentModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CommentAssembler implements RepresentationModelAssembler<Comment, CommentModel> {

    @Override
    public @NotNull CommentModel toModel(@NotNull Comment entity) {
        CommentModel model = new CommentModel(entity);
        model.setChilds(toCollectionModel(entity.getChilds()));

        model.add(linkTo(methodOn(CommentController.class).getOne(entity.getId())).withSelfRel());

        return model;
    }
}
