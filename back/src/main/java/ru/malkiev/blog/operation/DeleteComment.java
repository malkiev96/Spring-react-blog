package ru.malkiev.blog.operation;

import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Comment;

import java.util.function.UnaryOperator;

@Component
public class DeleteComment implements UnaryOperator<Comment> {

    @Override
    public Comment apply(Comment comment) {
        comment.setDeleted(true);
        comment.setMessage("Сообщение удалено");
        return comment;
    }
}
