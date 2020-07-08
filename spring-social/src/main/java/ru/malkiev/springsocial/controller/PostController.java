package ru.malkiev.springsocial.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;
import ru.malkiev.springsocial.assembler.PostAssembler;
import ru.malkiev.springsocial.assembler.PostDetailAssembler;
import ru.malkiev.springsocial.entity.*;
import ru.malkiev.springsocial.exception.ResourceNotFoundException;
import ru.malkiev.springsocial.model.PostDetailModel;
import ru.malkiev.springsocial.model.PostModel;
import ru.malkiev.springsocial.payload.PostRequest;
import ru.malkiev.springsocial.repository.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final FileRepository fileRepository;
    private final PostAssembler postAssembler;
    private final PostDetailAssembler detailAssembler;

    @GetMapping("/posts/{id}")
    public PostDetailModel getOne(@PathVariable int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return detailAssembler.toModel(post);
    }

    @GetMapping("/posts")
    public CollectionModel<PostModel> getPosts(@PageableDefault Pageable pageable) {
        Page<Post> page = postRepository.findAllByPostedIsTrue(pageable);
        return postAssembler.toPagedModel(page);
    }

    @GetMapping("/posts/user/{id}")
    public CollectionModel<PostModel> getPostsByUser(@PathVariable int id,
                                                     @PageableDefault Pageable pageable) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        Page<Post> page = postRepository.findAllByCreatedByAndPostedIsTrue(user, pageable);
        return postAssembler.toPagedModel(page);
    }

    @GetMapping("/posts/tag")
    public CollectionModel<PostModel> getPostsByTags(@RequestParam("ids") List<Integer> ids,
                                                     @PageableDefault Pageable pageable) {
        List<Tag> tags = tagRepository.findAllById(ids);
        Page<Post> page = postRepository.findAllByTagsInAndPostedIsTrue(tags, pageable);
        return postAssembler.toPagedModel(page);
    }

    @GetMapping("/posts/category")
    public CollectionModel<PostModel> getPostsByCat(@RequestParam("ids") List<Integer> ids,
                                                    @PageableDefault Pageable pageable) {
        List<Category> categories = categoryRepository.findAllById(ids);
        Page<Post> page = postRepository.findAllByCategoryInAndPostedIsTrue(categories, pageable);
        return postAssembler.toPagedModel(page);
    }

    @PostMapping("posts")
    public PostDetailModel createPost(@Valid @RequestBody PostRequest postRequest) {
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setText(postRequest.getText());
        if (postRequest.getPreviewId() != null) {
            Image image = imageRepository.findById(postRequest.getPreviewId())
                    .orElseThrow(() -> new ResourceNotFoundException("Image", "id", postRequest.getPreviewId()));
            post.setImagePreview(image);
        }
        if (postRequest.isPosted()) {
            post.setPosted(true);
            post.setPostedDate(new Date());
        }
        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postRequest.getCategoryId()));
        post.setCategory(category);

        List<Tag> tags = tagRepository.findAllById(postRequest.getTagIds());
        post.setTags(tags);

        if (postRequest.getFileIds() != null) {
            List<File> files = fileRepository.findAllById(postRequest.getFileIds());
            post.setFiles(files);
        }


        post = postRepository.save(post);
        return detailAssembler.toModel(post);
    }
}
