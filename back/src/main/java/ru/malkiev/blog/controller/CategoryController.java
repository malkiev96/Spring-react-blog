package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.CategoryModelAssembler;
import ru.malkiev.blog.assembler.CategoryParentModelAssembler;
import ru.malkiev.blog.dto.CategoryDto;
import ru.malkiev.blog.entity.Category;
import ru.malkiev.blog.exception.CategoryNotFoundException;
import ru.malkiev.blog.model.CategoryModel;
import ru.malkiev.blog.model.CategoryParentModel;
import ru.malkiev.blog.operation.CreateCategory;
import ru.malkiev.blog.repository.CategoryRepository;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class CategoryController {

    private final CreateCategory createCategory;
    private final CategoryRepository repository;
    private final CategoryModelAssembler assembler;
    private final CategoryParentModelAssembler parentAssembler;

    @GetMapping("/categories")
    public ResponseEntity<CollectionModel<CategoryModel>> categories() {
        return ResponseEntity.ok(
                assembler.toCollectionModel(
                        repository.findAllByParentIsNull()
                )
        );
    }

    @GetMapping("/categories/all")
    public ResponseEntity<CollectionModel<CategoryParentModel>> withParents() {
        return ResponseEntity.ok(
                parentAssembler.toCollectionModel(
                        repository.findAll()
                )
        );
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryModel> create(@Valid @RequestBody CategoryDto dto) {
        return Optional.of(dto)
                .map(createCategory)
                .map(repository::save)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        Category category = repository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(id));
        repository.delete(category);
        return ResponseEntity.accepted().build();
    }

}
