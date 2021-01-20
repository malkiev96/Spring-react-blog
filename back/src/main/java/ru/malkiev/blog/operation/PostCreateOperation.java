package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.dto.PostDto;
import ru.malkiev.blog.entity.Category;
import ru.malkiev.blog.entity.Document;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.exception.CategoryNotFoundException;
import ru.malkiev.blog.exception.DocumentNotFoundException;
import ru.malkiev.blog.exception.PostNotFoundException;
import ru.malkiev.blog.repository.CategoryRepository;
import ru.malkiev.blog.repository.DocumentRepository;
import ru.malkiev.blog.repository.PostRepository;
import ru.malkiev.blog.repository.TagRepository;

import javax.validation.Valid;
import java.util.function.Function;

import static ru.malkiev.blog.entity.PostStatus.CREATED;
import static ru.malkiev.blog.entity.PostStatus.PUBLISHED;

@Component
@AllArgsConstructor
public class PostCreateOperation implements Function<PostDto, Post> {

    private final PostRepository repository;
    private final DocumentRepository documentRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    public Post apply(@Valid PostDto postDto) {
        Post post = getPost(postDto.getId());

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setText(postDto.getText());
        post.setPreview(getPreview(postDto.getPreviewId()));
        post.setCategory(getCategory(postDto.getCategoryId()));
        post.setDocuments(documentRepository.findAllById(postDto.getDocumentIds()));
        post.setTags(tagRepository.findAllById(postDto.getTagIds()));
        post.setStatus(postDto.isPosted() ? PUBLISHED : CREATED);

        return repository.save(post);
    }

    private Category getCategory(@NonNull Integer catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId));
    }

    private Post getPost(@Nullable Integer postId) {
        return postId == null ? new Post() : repository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    private Document getPreview(@Nullable Long previewId) {
        return previewId == null ? null : documentRepository.findById(previewId)
                .orElseThrow(() -> new DocumentNotFoundException(previewId));
    }
}
