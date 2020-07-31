package ru.malkiev.springsocial.service;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.malkiev.springsocial.entity.Image;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.entity.Tag;
import ru.malkiev.springsocial.exception.CategoryNotFoundException;
import ru.malkiev.springsocial.exception.ImageNotFoundException;
import ru.malkiev.springsocial.model.payload.PostRequest;
import ru.malkiev.springsocial.repository.CategoryRepository;
import ru.malkiev.springsocial.repository.ImageRepository;
import ru.malkiev.springsocial.repository.PostRepository;
import ru.malkiev.springsocial.repository.TagRepository;

import java.util.List;
import java.util.Optional;

import static ru.malkiev.springsocial.entity.Post.Status.*;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public Post create(PostRequest postRequest) {
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setText(postRequest.getText());
        if (postRequest.getPreviewId() != null) {
            Image image = imageRepository.findById(postRequest.getPreviewId())
                    .orElseThrow(() -> new ImageNotFoundException(postRequest.getPreviewId()));
            post.setPreview(image);
        }
        if (!postRequest.getImageIds().isEmpty()) {
            post.setImages(imageRepository.findAllById(postRequest.getImageIds()));
        }
        post.setCategory(categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(postRequest.getCategoryId())));

        List<Tag> tags = tagRepository.findAllById(postRequest.getTagIds());
        post.setTags(tags);
        post.setStatus(postRequest.isPosted() ? PUBLISHED : CREATED);

        return postRepository.save(post);
    }

    public Post delete(@NotNull Post post) {
        post.setStatus(DELETED);
        return postRepository.save(post);
    }

    public Post publish(@NotNull Post post) {
        if (post.getStatus().equals(CREATED)) {
            post.setStatus(PUBLISHED);
            return postRepository.save(post);
        } else throw new IllegalArgumentException("Can't publish post by status " + post.getStatus());
    }

    public Post hide(@NotNull Post post) {
        if (post.getStatus().equals(PUBLISHED)) {
            post.setStatus(CREATED);
            return postRepository.save(post);
        } else throw new IllegalArgumentException("Can't hide post by status " + post.getStatus());
    }

    public Page<Post> findAll(Specification<Post> specification, Pageable pageable) {
        return postRepository.findAll(specification, pageable);
    }

    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

}
