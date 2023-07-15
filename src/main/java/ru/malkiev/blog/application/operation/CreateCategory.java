package ru.malkiev.blog.application.operation;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.api.dto.CategoryDto;
import ru.malkiev.blog.application.exception.CategoryNotFoundException;
import ru.malkiev.blog.domain.entity.Category;
import ru.malkiev.blog.domain.repository.CategoryRepository;

/**
 * Create or update category
 */
@Component
@AllArgsConstructor
public class CreateCategory implements Function<CategoryDto, Category> {

  private final CategoryRepository repository;

  @Override
  public Category apply(CategoryDto dto) {
    Category category = getCategory(dto);
    category.setCode(dto.getCode());
    category.setName(dto.getName());
    category.setParent(getParent(dto));

    return category;
  }

  private Category getCategory(CategoryDto dto) {
    Integer categoryId = dto.getCategoryId();
    return categoryId == null ? new Category() :
        repository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
  }

  private Category getParent(CategoryDto dto) {
    Integer parentId = dto.getParentId();
    if (parentId != null) {
      return repository.findById(parentId)
          .orElseThrow(() -> new CategoryNotFoundException(parentId));
    }
    return null;
  }
}
