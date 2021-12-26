package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.Category;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class CategoryModel extends RepresentationModel<CategoryModel> {

    private Integer id;
    private String name;
    private String code;
    private List<CategoryModel> childs = new ArrayList<>();

    public CategoryModel(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.code = category.getCode();
        category.getChilds().forEach(child -> this.childs.add(new CategoryModel(child)));
    }
}
