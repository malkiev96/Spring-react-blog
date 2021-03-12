package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.assembler.CategoryModelAssembler;
import ru.malkiev.blog.model.CategoryModel;
import ru.malkiev.blog.repository.CategoryRepository;

@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryRepository repository;
    private final CategoryModelAssembler assembler;

    @GetMapping("/categories")
    public ResponseEntity<CollectionModel<CategoryModel>> categories() {
        return ResponseEntity.ok(
                assembler.toCollectionModel(
                        repository.findAllByParentIsNull()
                )
        );
    }

}
