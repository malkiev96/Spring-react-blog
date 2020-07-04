package com.example.springsocial.assembler;

import com.example.springsocial.controller.CategoryController;
import com.example.springsocial.entity.Category;
import com.example.springsocial.model.CategoryModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoryAssembler implements RepresentationModelAssembler<Category, CategoryModel> {

    @Override
    public @NotNull CategoryModel toModel(@NotNull Category entity) {
        CategoryModel model = new CategoryModel(entity);
        model.setChilds(toCollectionModel(entity.getChilds()));

        model.add(linkTo(methodOn(CategoryController.class).getOne(entity.getId())).withSelfRel());

        return model;
    }
}
