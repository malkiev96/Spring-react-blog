package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.TagModelAssembler;
import ru.malkiev.blog.dto.TagDto;
import ru.malkiev.blog.model.TagModel;
import ru.malkiev.blog.operation.CreateTag;
import ru.malkiev.blog.repository.TagRepository;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class TagController {

    private final TagRepository repository;
    private final TagModelAssembler assembler;
    private final CreateTag createTag;

    @GetMapping("/tags")
    public ResponseEntity<CollectionModel<TagModel>> allTags() {
        return ResponseEntity.ok(
                assembler.toCollectionModel(
                        repository.findAll()
                )
        );
    }

    @PostMapping("/tags")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<TagModel> createTag(@Valid @RequestBody TagDto dto) {
        return Optional.of(dto)
                .map(createTag)
                .map(repository::save)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/tags/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deleteTag(@PathVariable Integer id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
