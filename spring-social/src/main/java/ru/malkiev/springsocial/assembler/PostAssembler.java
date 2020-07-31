package ru.malkiev.springsocial.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.link.PostLinks;
import ru.malkiev.springsocial.model.PostModel;


@Component
@AllArgsConstructor
public class PostAssembler implements RepresentationModelAssembler<Post, PostModel> {

    private final PagedResourcesAssembler<Post> pagedAssembler;

    public PagedModel<PostModel> toPagedModel(Page<Post> page){
        return pagedAssembler.toModel(page, this);
    }

    @Override
    public @NotNull PostModel toModel(@NotNull Post entity) {
        PostModel model = new PostModel(entity);
        model.add(PostLinks.linkToDetailPost(entity));
        return model;
    }
}
