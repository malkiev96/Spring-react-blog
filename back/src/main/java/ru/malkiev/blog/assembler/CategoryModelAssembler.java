package ru.malkiev.blog.assembler;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Category;
import ru.malkiev.blog.model.CategoryModel;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, CategoryModel> {

    @Override
    public @NotNull CategoryModel toModel(@NotNull Category entity) {
        CategoryModel model = new CategoryModel(entity);
        // TODO links
        return model;
    }
}
