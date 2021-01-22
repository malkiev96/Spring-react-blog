package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.dto.CategoryDto;
import ru.malkiev.blog.entity.Category;
import ru.malkiev.blog.exception.CategoryNotFoundException;
import ru.malkiev.blog.repository.CategoryRepository;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class CategoryCreateOperation implements Function<CategoryDto, Category> {

    private final CategoryRepository repository;

    @Override
    public Category apply(CategoryDto dto) {
        var category = new Category();
        category.setCode(dto.getCode());
        category.setName(dto.getName());

        Integer parentId = dto.getParentId();
        if (parentId != null) {
            Category parent = repository.findById(parentId)
                    .orElseThrow(() -> new CategoryNotFoundException(parentId));
            category.setParent(parent);
        }
        return category;
    }
}
