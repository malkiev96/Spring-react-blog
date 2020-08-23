package ru.malkiev.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "COMMENTS")
@Data
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MESSAGE", nullable = false)
    @NotNull
    @Size(max = 1000)
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "POST_ID", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Comment parent;

    @Column(name = "COMMENT_DELETED")
    private boolean deleted = false;

    @OneToMany(mappedBy = "parent")
    private List<Comment> childs = new ArrayList<>();
}
