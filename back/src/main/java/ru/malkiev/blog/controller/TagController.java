package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.assembler.TagModelAssembler;
import ru.malkiev.blog.model.TagModel;
import ru.malkiev.blog.repository.TagRepository;

@RestController
@AllArgsConstructor
public class TagController {

    private final TagRepository repository;
    private final TagModelAssembler assembler;

    @GetMapping("/tags")
    public ResponseEntity<CollectionModel<TagModel>> allTags() {
        return ResponseEntity.ok(
                assembler.toCollectionModel(
                        repository.findAll()
                )
        );
    }
}
