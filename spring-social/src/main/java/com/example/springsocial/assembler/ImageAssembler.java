package com.example.springsocial.assembler;

import com.example.springsocial.entity.Image;
import com.example.springsocial.model.ImageModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ImageAssembler implements RepresentationModelAssembler<Image, ImageModel> {

    @Override
    public @NotNull ImageModel toModel(@NotNull Image entity) {
        ImageModel model = new ImageModel(entity);
        //todo links
        return model;
    }
}
