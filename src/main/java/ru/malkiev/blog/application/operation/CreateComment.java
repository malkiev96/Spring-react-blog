package ru.malkiev.blog.application.operation;

import java.util.function.Function;
import lombok.NonNull;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.domain.entity.Comment;
import ru.malkiev.blog.domain.entity.Post;

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
