package ru.malkiev.springsocial.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.malkiev.springsocial.entity.*;
import ru.malkiev.springsocial.entity.Post.Status;
import ru.malkiev.springsocial.exception.ResourceNotFoundException;
import ru.malkiev.springsocial.model.payload.PostRequest;
import ru.malkiev.springsocial.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public Post create(PostRequest postRequest, User currentUser) {
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setText(postRequest.getText());
        if (postRequest.getPreviewId() != null) {
            Image image = imageRepository.findById(postRequest.getPreviewId())
                    .orElseThrow(() -> new ResourceNotFoundException("Image", "id", postRequest.getPreviewId()));
            post.setPreview(image);
        }
        if (!postRequest.getImageIds().isEmpty()){
            post.setImages(imageRepository.findAllById(postRequest.getImageIds()));
        }
        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postRequest.getCategoryId()));
        post.setCategory(category);

        List<Tag> tags = tagRepository.findAllById(postRequest.getTagIds());
        post.setTags(tags);

        if (currentUser.isAdmin()) post.setStatus(postRequest.isPosted() ? Status.PUBLISHED : Status.CREATED);
        else post.setStatus(postRequest.isPosted() ? Status.PENDING : Status.CREATED);

        return postRepository.save(post);
    }

    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

}
