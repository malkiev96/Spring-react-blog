package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.springsocial.assembler.PostAssembler;
import ru.malkiev.springsocial.assembler.PostDetailAssembler;
import ru.malkiev.springsocial.entity.Category;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.entity.Tag;
import ru.malkiev.springsocial.exception.ResourceNotFoundException;
import ru.malkiev.springsocial.model.PostDetailModel;
import ru.malkiev.springsocial.model.PostModel;
import ru.malkiev.springsocial.payload.PostRequest;
import ru.malkiev.springsocial.repository.CategoryRepository;
import ru.malkiev.springsocial.repository.TagRepository;
import ru.malkiev.springsocial.repository.UserRepository;
import ru.malkiev.springsocial.service.PostService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class PostController {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final PostAssembler postAssembler;
    private final PostDetailAssembler detailAssembler;
    private final PostService postService;

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailModel> getOne(@PathVariable int id) {
        return postService.getPost(id)
                .map(detailAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }

    @GetMapping("/posts")
    public CollectionModel<PostModel> getPosts(@PageableDefault Pageable pageable) {
        Page<Post> page = postService.getPublishedPosts(pageable);
        return postAssembler.toPagedModel(page);
    }

    @GetMapping("/posts/user/{id}")
    public CollectionModel<PostModel> getPostsByUser(@PathVariable int id,
                                                     @PageableDefault Pageable pageable) {
        return userRepository.findById(id)
                .map(user -> postService.getPostsByUser(pageable, user))
                .map(postAssembler::toPagedModel)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @GetMapping("/posts/tag")
    public CollectionModel<PostModel> getPostsByTags(@RequestParam("ids") List<Integer> ids,
                                                     @PageableDefault Pageable pageable) {
        List<Tag> tags = tagRepository.findAllById(ids);
        Page<Post> page = postService.getPostsByTags(pageable, tags);
        return postAssembler.toPagedModel(page);
    }

    @GetMapping("/posts/category")
    public CollectionModel<PostModel> getPostsByCat(@RequestParam("ids") List<Integer> ids,
                                                    @PageableDefault Pageable pageable) {
        List<Category> categories = categoryRepository.findAllById(ids);
        Page<Post> page = postService.getPostsByCategories(pageable, categories);
        return postAssembler.toPagedModel(page);
    }

    @PostMapping("posts")
    public PostDetailModel createPost(@Valid @RequestBody PostRequest postRequest) {
        Post post = postService.create(postRequest);
        return detailAssembler.toModel(post);
    }
}
