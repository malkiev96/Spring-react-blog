package ru.malkiev.springsocial.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.springsocial.entity.Comment;

@EqualsAndHashCode(callSuper = false)
@Data
public class CommentModel extends RepresentationModel<CommentModel> {

    private int id;
    private String message;
    private boolean deleted;
    private AuditorModel auditor;
    private CollectionModel<CommentModel> childs;

    public CommentModel(@NotNull Comment comment) {
        this.id = comment.getId();
        this.message = comment.getMessage();
        this.deleted = comment.isDeleted();
        this.auditor = new AuditorModel(comment);
    }
}
