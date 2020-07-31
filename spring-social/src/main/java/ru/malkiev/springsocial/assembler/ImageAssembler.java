package ru.malkiev.springsocial.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.Image;
import ru.malkiev.springsocial.model.ImageModel;

import static ru.malkiev.springsocial.link.ImageLinks.linkToImage;

@Component
public class ImageAssembler implements RepresentationModelAssembler<Image, ImageModel> {

    @Override
    public @NotNull ImageModel toModel(@NotNull Image entity) {
        ImageModel model = new ImageModel(entity);
        model.add(linkToImage(entity).withSelfRel());

        return model;
    }

}
