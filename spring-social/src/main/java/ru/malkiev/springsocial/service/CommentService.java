package ru.malkiev.springsocial.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.malkiev.springsocial.entity.Comment;
import ru.malkiev.springsocial.entity.Post;
import ru.malkiev.springsocial.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository repository;

    public Optional<Comment> getComment(int id) {
        return repository.findById(id);
    }

    public List<Comment> getComments(Post post, Sort sort) {
        return repository.findAllByPostAndParentIsNull(post, sort);
    }

    public Comment delete(Comment comment) {
        comment.setDeleted(true);
        comment.setMessage("Сообщение удалено");
        return repository.save(comment);
    }

    public Comment create(Post post, String message) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setDeleted(false);
        comment.setMessage(message);
        return repository.save(comment);
    }

    public Comment reply(Post post, String message, Comment parent) {
        Comment comment = new Comment();
        comment.setParent(parent);
        comment.setPost(post);
        comment.setDeleted(false);
        comment.setMessage(message);
        return repository.save(comment);
    }
}
