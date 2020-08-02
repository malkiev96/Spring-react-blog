package ru.malkiev.springsocial.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.springsocial.entity.Comment;
import ru.malkiev.springsocial.repository.CommentRepository;

import java.util.function.UnaryOperator;

@Component
@AllArgsConstructor
public class CommentDeleteOperation implements UnaryOperator<Comment> {

    private final CommentRepository repository;

    @Override
    public Comment apply(Comment comment) {
        comment.setDeleted(true);
        comment.setMessage("Сообщение удалено");
        return repository.save(comment);
    }
}
