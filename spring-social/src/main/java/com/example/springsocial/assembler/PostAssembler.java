package com.example.springsocial.assembler;

import com.example.springsocial.entity.Post;
import com.example.springsocial.model.PostModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PostAssembler implements RepresentationModelAssembler<Post, PostModel> {

    @Override
    public @NotNull PostModel toModel(@NotNull Post entity) {
        PostModel model = new PostModel(entity);

        return model;
    }
}
