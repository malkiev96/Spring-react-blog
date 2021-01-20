package ru.malkiev.blog.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.blog.assembler.ContactMessageAssembler;
import ru.malkiev.blog.dto.ContactMessageDto;
import ru.malkiev.blog.model.ContactMessageModel;
import ru.malkiev.blog.repository.ContactMessageRepository;

import javax.validation.Valid;
import java.util.Optional;

import static ru.malkiev.blog.dto.ContactMessageDto.from;

@RestController
@AllArgsConstructor
public class ContactController {

    private final ContactMessageRepository repository;
    private final ContactMessageAssembler assembler;

    @GetMapping("/contacts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<ContactMessageModel>> messages(@PageableDefault Pageable pageable) {
        return Optional.of(repository.findAll(pageable))
                .map(assembler::toPagedModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/contacts/{id}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        repository.deleteById(id);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/contacts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ContactMessageModel> create(@RequestBody @Valid ContactMessageDto request) {
        return ResponseEntity.ok(assembler.toModel(repository.save(from(request))));
    }
}
