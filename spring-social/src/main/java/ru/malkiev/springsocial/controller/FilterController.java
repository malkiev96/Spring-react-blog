package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.springsocial.model.FilterModel;
import ru.malkiev.springsocial.repository.CategoryRepository;
import ru.malkiev.springsocial.repository.TagRepository;

@RestController
@AllArgsConstructor
public class FilterController {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @GetMapping("/filters")
    private ResponseEntity<FilterModel> getFilters(){
        FilterModel filterModel = new FilterModel();
        categoryRepository.findAllByParentIsNull().forEach(filterModel::addCategories);
        tagRepository.findAll().forEach(filterModel::addTags);

        return ResponseEntity.ok(filterModel);

    }
}
