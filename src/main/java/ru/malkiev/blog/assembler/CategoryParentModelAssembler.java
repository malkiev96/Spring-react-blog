package ru.malkiev.blog.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Category;
import ru.malkiev.blog.model.CategoryParentModel;

@Component
public class CategoryParentModelAssembler implements RepresentationModelAssembler<Category, CategoryParentModel> {

    @Override
    public @NotNull CategoryParentModel toModel(@NotNull Category entity) {
        return new CategoryParentModel(entity);
    }
}
