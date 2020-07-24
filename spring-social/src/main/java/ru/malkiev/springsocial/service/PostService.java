package ru.malkiev.springsocial.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.malkiev.springsocial.entity.*;
import ru.malkiev.springsocial.exception.ResourceNotFoundException;
import ru.malkiev.springsocial.payload.PostRequest;
import ru.malkiev.springsocial.repository.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final FileRepository fileRepository;

    public Post create(PostRequest postRequest) {
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

        return postRepository.save(post);
    }

    public Optional<Post> getPost(int id) {
        return postRepository.findById(id);
    }

    public Page<Post> getPublishedPosts(Pageable pageable) {
        return postRepository.findAllByPostedIsTrue(pageable);
    }

    public Page<Post> getPostsByUser(Pageable pageable, User createdBy) {
        return postRepository.findAllByCreatedByAndPostedIsTrue(createdBy, pageable);
    }

    public Page<Post> getPostsByTags(Pageable pageable, List<Tag> tags) {
        return postRepository.findAllByTagsInAndPostedIsTrue(tags, pageable);
    }

    public Page<Post> getPostsByCategories(Pageable pageable, List<Category> categories) {
        return postRepository.findAllByCategoryInAndPostedIsTrue(categories, pageable);
    }
}
