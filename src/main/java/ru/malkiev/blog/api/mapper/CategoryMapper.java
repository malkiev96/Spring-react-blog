package ru.malkiev.blog.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import ru.malkiev.blog.api.model.CategoryModel;
import ru.malkiev.blog.api.model.CategoryParentModel;
import ru.malkiev.blog.domain.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  CategoryModel toModel(Category category);

  List<CategoryModel> toCollectionModel(List<Category> categories);

  CategoryParentModel toParentModel(Category category);

  List<CategoryParentModel> toParentCollectionModel(List<Category> categories);

}
