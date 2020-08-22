package ru.malkiev.springsocial.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.entity.Tag;
import ru.malkiev.springsocial.exception.CategoryNotFoundException;
import ru.malkiev.springsocial.exception.ImageNotFoundException;
import ru.malkiev.springsocial.exception.PostNotFoundException;
import ru.malkiev.springsocial.model.payload.PostRequest;
import ru.malkiev.springsocial.repository.CategoryRepository;
import ru.malkiev.springsocial.repository.ImageRepository;
import ru.malkiev.springsocial.repository.PostRepository;
import ru.malkiev.springsocial.repository.TagRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;

import static ru.malkiev.springsocial.entity.Post.Status.CREATED;
import static ru.malkiev.springsocial.entity.Post.Status.PUBLISHED;

@Component
@AllArgsConstructor
public class PostCreateOperation implements Function<PostRequest, Post> {

    private final PostRepository repository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    public Post apply(@Valid PostRequest postRequest) {
        Post post;
        if (postRequest.getId() != null) post = repository.findById(postRequest.getId())
                .orElseThrow(() -> new PostNotFoundException(postRequest.getId()));
        else post = new Post();

        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setText(postRequest.getText());
        post.setPreview(postRequest.getPreviewId() != null ? imageRepository
                .findById(postRequest.getPreviewId())
                .orElseThrow(() -> new ImageNotFoundException(postRequest.getPreviewId())) : null);
        post.setImages(imageRepository.findAllById(postRequest.getImageIds()));
        post.setCategory(categoryRepository
                .findById(postRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(postRequest.getCategoryId())));

        List<Tag> tags = tagRepository.findAllById(postRequest.getTagIds());
        post.setTags(tags);
        post.setStatus(postRequest.isPosted() ? PUBLISHED : CREATED);

        return repository.save(post);
    }
}
