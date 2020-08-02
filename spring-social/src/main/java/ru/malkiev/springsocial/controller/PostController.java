package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.malkiev.springsocial.assembler.PostAssembler;
import ru.malkiev.springsocial.assembler.PostDetailAssembler;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.exception.PostNotFoundException;
import ru.malkiev.springsocial.model.PostDetailModel;
import ru.malkiev.springsocial.model.PostModel;
import ru.malkiev.springsocial.repository.PostRepository;
import ru.malkiev.springsocial.specification.PostSpecification;

@RestController
@AllArgsConstructor
public class PostController {

    private final PostRepository repository;
    private final PostAssembler postAssembler;
    private final PostDetailAssembler detailAssembler;

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailModel> getOne(@PathVariable int id) {
        return repository.findById(id)
                .map(Post::incrementView)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @GetMapping("/posts")
    public ResponseEntity<PagedModel<PostModel>> getPosts(PostSpecification spec,
                                                          @PageableDefault Pageable pageable) {
        return spec.get()
                .map(s -> repository.findAll(s, pageable))
                .map(postAssembler::toPagedModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
