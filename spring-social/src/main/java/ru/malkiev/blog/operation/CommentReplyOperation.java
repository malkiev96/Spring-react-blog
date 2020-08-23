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
public class CommentReplyOperation implements Function<Pair<Pair<Post, String>, Comment>, Comment> {

    private final CommentRepository repository;

    @Override
    public Comment apply(Pair<Pair<Post, String>, Comment> pair) {
        Comment parent = pair.getSecond();
        Post post = pair.getFirst().getFirst();
        String message = pair.getFirst().getSecond();
        Objects.requireNonNull(parent);
        Objects.requireNonNull(post);
        Objects.requireNonNull(message);

        Comment comment = new Comment();
        comment.setParent(parent);
        comment.setPost(post);
        comment.setDeleted(false);
        comment.setMessage(message);
        return repository.save(comment);
    }
}
