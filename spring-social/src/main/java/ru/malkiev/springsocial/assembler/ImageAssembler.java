package ru.malkiev.springsocial.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.controller.ImageController;
import ru.malkiev.springsocial.entity.Image;
import ru.malkiev.springsocial.model.ImageModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ImageAssembler implements RepresentationModelAssembler<Image, ImageModel> {

    @Override
    public @NotNull ImageModel toModel(@NotNull Image entity) {
        ImageModel model = new ImageModel(entity);
        model.setUrl(createUrl(entity));
        model.add(createLinkToSelf(entity));

        return model;
    }

    private Link createLinkToSelf(Image entity) {
        return linkTo(methodOn(ImageController.class).getOne(entity.getId())).withSelfRel();
    }

    private String createUrl(Image entity) {
        return linkTo(methodOn(ImageController.class).getFile(entity.getName(), null))
                .toUri().toString();
    }
}
