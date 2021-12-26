package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.dto.CategoryDto;
import ru.malkiev.blog.entity.Category;
import ru.malkiev.blog.exception.CategoryNotFoundException;
import ru.malkiev.blog.repository.CategoryRepository;

import java.util.function.Function;

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
                repository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    private Category getParent(CategoryDto dto) {
        Integer parentId = dto.getParentId();
        if (parentId != null) {
            return repository.findById(parentId).orElseThrow(() -> new CategoryNotFoundException(parentId));
        }
        return null;
    }
}
