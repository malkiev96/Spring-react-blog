package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.TagModelAssembler;
import ru.malkiev.blog.dto.TagDto;
import ru.malkiev.blog.model.TagModel;
import ru.malkiev.blog.operation.TagCreateOperation;
import ru.malkiev.blog.repository.TagRepository;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class TagController {

    private final TagRepository repository;
    private final TagModelAssembler assembler;
    private final TagCreateOperation createOperation;

    @GetMapping("/tags")
    public ResponseEntity<CollectionModel<TagModel>> allTags() {
        return ResponseEntity.ok(
                assembler.toCollectionModel(
                        repository.findAll()
                )
        );
    }

    @PostMapping("/tags")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TagModel> createTag(@Valid @RequestBody TagDto dto) {
        return ResponseEntity.ok(
                assembler.toModel(
                        repository.save(createOperation.apply(dto))
                )
        );
    }

    @DeleteMapping("/tags/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteTag(@PathVariable Integer id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
