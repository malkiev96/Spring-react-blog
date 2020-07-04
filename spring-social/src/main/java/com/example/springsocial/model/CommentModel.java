package com.example.springsocial.model;

import com.example.springsocial.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = false)
@Data
public class CommentModel extends RepresentationModel<CommentModel> {

    private int id;
    private String message;
    private AuditorModel auditor;
    private CollectionModel<CommentModel> childs;

    public CommentModel(@NotNull Comment comment) {
        this.id = comment.getId();
        this.message = comment.getMessage();
        this.auditor = new AuditorModel(comment);
    }
}
