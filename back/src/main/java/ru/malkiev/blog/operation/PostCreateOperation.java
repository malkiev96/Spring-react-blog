package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Category;
import ru.malkiev.blog.entity.Image;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.exception.CategoryNotFoundException;
import ru.malkiev.blog.exception.ImageNotFoundException;
import ru.malkiev.blog.exception.PostNotFoundException;
import ru.malkiev.blog.model.payload.PostDto;
import ru.malkiev.blog.repository.CategoryRepository;
import ru.malkiev.blog.repository.ImageRepository;
import ru.malkiev.blog.repository.PostRepository;
import ru.malkiev.blog.repository.TagRepository;

import javax.validation.Valid;
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
        Post post = getPost(postDto.getId());

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setText(postDto.getText());
        post.setPreview(getPreview(postDto.getPreviewId()));
        post.setCategory(getCategory(postDto.getId()));

        post.setImages(imageRepository.findAllById(postDto.getImageIds()));
        post.setTags(tagRepository.findAllById(postDto.getTagIds()));
        post.setStatus(postDto.isPosted() ? PUBLISHED : CREATED);

        return repository.save(post);
    }

    private Category getCategory(@NonNull Integer catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId));
    }

    private Post getPost(@Nullable Integer postId) {
        return postId != null
                ? repository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId))
                : new Post();
    }

    private Image getPreview(@Nullable Integer previewId) {
        return previewId != null
                ? imageRepository.findById(previewId).orElseThrow(() -> new ImageNotFoundException(previewId))
                : null;
    }
}
