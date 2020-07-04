package com.example.springsocial.assembler;

import com.example.springsocial.entity.Tag;
import com.example.springsocial.model.TagModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TagAssembler implements RepresentationModelAssembler<Tag, TagModel> {

    @Override
    public @NotNull TagModel toModel(@NotNull Tag entity) {
        TagModel model = new TagModel(entity);

        return model;
    }
}
