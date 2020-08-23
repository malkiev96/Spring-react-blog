package ru.malkiev.blog.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Image;
import ru.malkiev.blog.model.ImageModel;

import static ru.malkiev.blog.link.ImageLinks.linkToImage;

@Component
@AllArgsConstructor
public class ImageAssembler implements RepresentationModelAssembler<Image, ImageModel> {

    private final PagedResourcesAssembler<Image> pagedAssembler;

    public PagedModel<ImageModel> toPagedModel(Page<Image> page) {
        return pagedAssembler.toModel(page, this);
    }

    @Override
    public @NotNull ImageModel toModel(@NotNull Image entity) {
        ImageModel model = new ImageModel(entity);
        model.add(linkToImage(entity).withSelfRel());

        return model;
    }

}
