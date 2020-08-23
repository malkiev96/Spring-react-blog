package ru.malkiev.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "POST_RATING")
public class PostRating extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Column(name = "STAR")
    private int star;

    public static PostRating of(Post post, int star){
        PostRating rating = new PostRating();
        rating.setPost(post);
        rating.setStar(star);
        return rating;
    }
}
