package ru.malkiev.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "POST_LIKES")
public class PostLike extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    @NotNull
    private Post post;

    public static PostLike of(Post post) {
        PostLike like = new PostLike();
        like.setPost(post);
        return like;
    }
}
