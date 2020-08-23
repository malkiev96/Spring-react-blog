package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.model.FilterModel;
import ru.malkiev.blog.repository.CategoryRepository;
import ru.malkiev.blog.repository.TagRepository;

@RestController
@AllArgsConstructor
public class RootController {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @GetMapping("/filters")
    private ResponseEntity<FilterModel> getFilters(){
        FilterModel filterModel = new FilterModel(
                categoryRepository.findAllByParentIsNull(),
                tagRepository.findAll());

        return ResponseEntity.ok(filterModel);
    }
}
