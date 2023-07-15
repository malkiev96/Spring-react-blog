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
import ru.malkiev.blog.api.dto.TagDto;
import ru.malkiev.blog.api.mapper.TagMapper;
import ru.malkiev.blog.api.model.TagModel;
import ru.malkiev.blog.application.operation.CreateTag;
import ru.malkiev.blog.domain.repository.TagRepository;

@RestController
@RequiredArgsConstructor
public class TagController {

  private final TagRepository repository;
  private final CreateTag createTag;
  private final TagMapper mapper;

  @GetMapping("/tags")
  public ResponseEntity<List<TagModel>> allTags() {
    return ResponseEntity.ok(
        mapper.toCollectionModel(
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
        .map(mapper::toModel)
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
