package ru.malkiev.blog.operation;

import lombok.NonNull;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Comment;
import ru.malkiev.blog.entity.Post;

import java.util.function.Function;

@Component
public class CreateComment implements Function<Pair<Post, String>, Comment> {

    @Override
    public Comment apply(@NonNull Pair<Post, String> pair) {
        Post post = pair.getFirst();
        String message = pair.getSecond();

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setDeleted(false);
        comment.setMessage(message);
        return comment;
    }
}
