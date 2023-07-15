package ru.malkiev.blog.application.operation;

import java.util.function.UnaryOperator;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.domain.entity.Comment;

@Component
public class DeleteComment implements UnaryOperator<Comment> {

  @Override
  public Comment apply(Comment comment) {
    comment.setDeleted(true);
    comment.setMessage("Сообщение удалено");
    return comment;
  }
}
