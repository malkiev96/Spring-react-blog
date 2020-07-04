package com.example.springsocial.controller;

import com.example.springsocial.assembler.TagAssembler;
import com.example.springsocial.entity.Tag;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.model.TagModel;
import com.example.springsocial.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TagController {

    private final TagRepository repository;
    private final TagAssembler assembler;

    @GetMapping("/tags/{id}")
    public TagModel getOne(@PathVariable int id) {
        Tag tag = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
        return assembler.toModel(tag);
    }

    @GetMapping("/tags")
    public CollectionModel<TagModel> getAll() {
        return assembler.toCollectionModel(repository.findAll());
    }

    @PostMapping("/tags/{id}")
    public TagModel update(@PathVariable int id,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "description", required = false) String description) {
        Tag tag = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
        if (name != null) tag.setName(name);
        if (description != null) tag.setDescription(description);
        return assembler.toModel(tag);
    }

}
