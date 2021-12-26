package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.Category;

import java.util.Optional;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class CategoryParentModel extends RepresentationModel<CategoryParentModel> {

    private Integer id;
    private String name;
    private String code;
    private CategoryParentModel parent;

    public CategoryParentModel(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.code = category.getCode();
        Optional.ofNullable(category.getParent()).ifPresent(p -> this.parent = new CategoryParentModel(p));
    }
}
