package ru.malkiev.blog.api.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.api.dto.CategoryDto;
import ru.malkiev.blog.api.mapper.CategoryMapper;
import ru.malkiev.blog.api.model.CategoryModel;
import ru.malkiev.blog.api.model.CategoryParentModel;
import ru.malkiev.blog.application.exception.CategoryNotFoundException;
import ru.malkiev.blog.application.operation.CreateCategory;
import ru.malkiev.blog.domain.entity.Category;
import ru.malkiev.blog.domain.repository.CategoryRepository;

@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CreateCategory createCategory;
  private final CategoryRepository repository;
  private final CategoryMapper categoryMapper;

  @GetMapping("/categories")
  public ResponseEntity<List<CategoryModel>> categories() {
    return ResponseEntity.ok(
        categoryMapper.toCollectionModel(
            repository.findAllByParentIsNull()
        )
    );
  }

  @GetMapping("/categories/all")
  public ResponseEntity<List<CategoryParentModel>> withParents() {
    return ResponseEntity.ok(
        categoryMapper.toParentCollectionModel(
            repository.findAll()
        )
    );
  }

  @Secured("ROLE_ADMIN")
  @PostMapping("/categories")
  public ResponseEntity<CategoryModel> create(@Valid @RequestBody CategoryDto dto) {
    return Optional.of(dto)
        .map(createCategory)
        .map(repository::save)
        .map(categoryMapper::toModel)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.noContent().build());
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/categories/{id}")
  public ResponseEntity<String> delete(@PathVariable Integer id) {
    Category category = repository.findById(id).orElseThrow(
        () -> new CategoryNotFoundException(id));
    repository.delete(category);
    return ResponseEntity.accepted().build();
  }

}
