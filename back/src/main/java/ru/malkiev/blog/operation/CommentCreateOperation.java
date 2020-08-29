package ru.malkiev.blog.operation;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Comment;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.repository.CommentRepository;

import java.util.Objects;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class CommentCreateOperation implements Function<Pair<Post, String>, Comment> {

    private final CommentRepository repository;

    @Override
    public Comment apply(Pair<Post, String> pair) {
        Post post = pair.getFirst();
        String message = pair.getSecond();
        Objects.requireNonNull(post);
        Objects.requireNonNull(message);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setDeleted(false);
        comment.setMessage(message);
        return repository.save(comment);
    }
}