package com.example.springsocial.model;

import com.example.springsocial.entity.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = false)
@Data
public class CategoryModel extends RepresentationModel<CategoryModel> {

    private int id;
    private String name;
    private String description;
    private CollectionModel<CategoryModel> childs;

    public CategoryModel(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
    }
}
