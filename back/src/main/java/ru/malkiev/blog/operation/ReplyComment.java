package ru.malkiev.blog.operation;

import lombok.NonNull;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Comment;
import ru.malkiev.blog.entity.Post;

import java.util.function.Function;

@Component
public class ReplyComment implements Function<Pair<Pair<Post, String>, Comment>, Comment> {

    @Override
    public Comment apply(@NonNull Pair<Pair<Post, String>, Comment> pair) {
        Comment parent = pair.getSecond();
        Post post = pair.getFirst().getFirst();
        String message = pair.getFirst().getSecond();

        Comment comment = new Comment();
        comment.setParent(parent);
        comment.setPost(post);
        comment.setDeleted(false);
        comment.setMessage(message);
        return comment;
    }
}
