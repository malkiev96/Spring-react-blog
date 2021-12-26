package ru.malkiev.blog.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Tag;
import ru.malkiev.blog.model.TagModel;

@Component
public class TagModelAssembler implements RepresentationModelAssembler<Tag, TagModel> {

    @Override
    public @NotNull TagModel toModel(@NotNull Tag entity) {
        TagModel model = new TagModel(entity);
        // TODO links
        return model;
    }
}
