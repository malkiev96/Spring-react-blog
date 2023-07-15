package ru.malkiev.blog.api.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.blog.api.dto.ContactMessageDto;
import ru.malkiev.blog.api.mapper.ContactMessageMapper;
import ru.malkiev.blog.api.model.ContactMessageModel;
import ru.malkiev.blog.domain.entity.ContactMessage;
import ru.malkiev.blog.domain.repository.ContactMessageRepository;

@RestController
@RequiredArgsConstructor
public class ContactController {

  private final ContactMessageRepository repository;
  private final ContactMessageMapper mapper;

  @GetMapping("/contacts")
  @Secured("ROLE_ADMIN")
  public ResponseEntity<Page<ContactMessageModel>> messages(@PageableDefault Pageable pageable) {
    return Optional.of(repository.findAll(pageable))
        .map(mapper::toPagedModel)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.noContent().build());
  }

  @PostMapping("/contacts/{id}/delete")
  @Secured("ROLE_ADMIN")
  public ResponseEntity<String> delete(@PathVariable Integer id) {
    repository.deleteById(id);
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/contacts")
  public ResponseEntity<ContactMessageModel> create(@RequestBody @Valid ContactMessageDto request) {
    ContactMessage message = new ContactMessage();
    message.setName(request.getName());
    message.setEmail(request.getEmail());
    message.setMessage(request.getMessage());
    message.setCreatedDate(LocalDateTime.now());
    return ResponseEntity.ok(mapper.toModel(repository.save(message)));
  }
}
