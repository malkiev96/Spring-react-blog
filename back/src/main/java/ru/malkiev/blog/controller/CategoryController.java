package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.CategoryModelAssembler;
import ru.malkiev.blog.dto.CategoryDto;
import ru.malkiev.blog.exception.CategoryNotFoundException;
import ru.malkiev.blog.model.CategoryModel;
import ru.malkiev.blog.operation.CategoryAddChildOperation;
import ru.malkiev.blog.operation.CategoryCreateOperation;
import ru.malkiev.blog.repository.CategoryRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryRepository repository;
    private final CategoryModelAssembler assembler;
    private final CategoryCreateOperation createCategory;
    private final CategoryAddChildOperation addChilds;

    @GetMapping("/categories")
    public ResponseEntity<CollectionModel<CategoryModel>> callCategories() {
        return ResponseEntity.ok(
                assembler.toCollectionModel(
                        repository.findAllByParentIsNull()
                )
        );
    }

    @PostMapping("/categories/{id}/childs")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CategoryModel> addChilds(@PathVariable Integer id,
                                                   @RequestParam("childs") List<Integer> childs) {
        return repository.findById(id)
                .map(cat -> Pair.of(childs, cat))
                .map(addChilds)
                .map(repository::save)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CategoryModel> create(@Valid @RequestBody CategoryDto dto) {
        return ResponseEntity.ok(
                assembler.toModel(
                        repository.save(createCategory.apply(dto))
                )
        );
    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
