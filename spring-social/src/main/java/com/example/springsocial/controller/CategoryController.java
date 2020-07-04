package com.example.springsocial.controller;

import com.example.springsocial.assembler.CategoryAssembler;
import com.example.springsocial.entity.Category;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.model.CategoryModel;
import com.example.springsocial.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryRepository repository;
    private final CategoryAssembler assembler;

    @GetMapping("/categories/{id}")
    public CategoryModel getOne(@PathVariable int id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return assembler.toModel(category);
    }

    @GetMapping("/categories")
    public CollectionModel<CategoryModel> getAll() {
        return assembler.toCollectionModel(repository.findAllByParentIsNull());
    }

    @PostMapping("/categories/{id}")
    public CategoryModel update(@PathVariable int id,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "description", required = false) String description) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        if (name != null) category.setName(name);
        if (description != null) category.setDescription(description);
        return assembler.toModel(category);
    }

    @PostMapping("/categories/{id}/parent/{parentId}")
    public CategoryModel updateParent(@PathVariable int id, @PathVariable int parentId) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        Category parent = repository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", parentId));
        category.setParent(parent);
        return assembler.toModel(category);
    }
}
