package ru.malkiev.springsocial.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.controller.PostController;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.model.PostModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
@AllArgsConstructor
public class PostAssembler implements RepresentationModelAssembler<Post, PostModel> {

    private final ImageAssembler imageAssembler;
    private final PagedResourcesAssembler<Post> pagedAssembler;

    public PagedModel<PostModel> toPagedModel(Page<Post> page){
        return pagedAssembler.toModel(page, this);
    }

    @Override
    public @NotNull PostModel toModel(@NotNull Post entity) {
        PostModel model = new PostModel(entity);
        if (entity.getImagePreview() != null) {
            model.setPreview(imageAssembler.toModel(entity.getImagePreview()));
        }

        model.add(linkTo(methodOn(PostController.class).getOne(entity.getId()))
                .withRel("detail")
                .withType("GET"));

        return model;
    }
}
