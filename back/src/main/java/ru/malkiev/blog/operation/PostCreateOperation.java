package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.Tag;
import ru.malkiev.blog.exception.CategoryNotFoundException;
import ru.malkiev.blog.exception.ImageNotFoundException;
import ru.malkiev.blog.exception.PostNotFoundException;
import ru.malkiev.blog.model.payload.PostDto;
import ru.malkiev.blog.repository.CategoryRepository;
import ru.malkiev.blog.repository.ImageRepository;
import ru.malkiev.blog.repository.PostRepository;
import ru.malkiev.blog.repository.TagRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Function;

import static ru.malkiev.blog.entity.Post.Status.CREATED;
import static ru.malkiev.blog.entity.Post.Status.PUBLISHED;

@Component
@AllArgsConstructor
public class PostCreateOperation implements Function<PostDto, Post> {

    private final PostRepository repository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    public Post apply(@Valid PostDto postDto) {
        Post post;
        if (postDto.getId() != null) post = repository.findById(postDto.getId())
                .orElseThrow(() -> new PostNotFoundException(postDto.getId()));
        else post = new Post();

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setText(postDto.getText());
        post.setPreview(postDto.getPreviewId() != null ? imageRepository
                .findById(postDto.getPreviewId())
                .orElseThrow(() -> new ImageNotFoundException(postDto.getPreviewId())) : null);
        post.setImages(imageRepository.findAllById(postDto.getImageIds()));
        post.setCategory(categoryRepository
                .findById(postDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(postDto.getCategoryId())));

        List<Tag> tags = tagRepository.findAllById(postDto.getTagIds());
        post.setTags(tags);
        post.setStatus(postDto.isPosted() ? PUBLISHED : CREATED);

        return repository.save(post);
    }
}
