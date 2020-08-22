package ru.malkiev.springsocial.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "POSTS")
@Data
public class Post extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "POST_TEXT", nullable = false, length = 4000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "PREVIEW_IMG_ID")
    private Image preview;

    @Column(name = "POST_STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "VIEW_COUNT")
    private int viewCount;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "POST_TAGS",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostRating> postRatings = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "POST_IMAGES",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "IMAGE_ID")
    )
    private List<Image> images = new ArrayList<>();

    public static Post incrementView(Post post) {
        post.setViewCount(post.getViewCount() + 1);
        return post;
    }

    public enum Status {
        /**
         * Пост создан, но не опубликован
         */
        CREATED,
        /**
         * Пост опубликован
         */
        PUBLISHED,
        /**
         * Пост удален
         */
        DELETED;

        public static final List<Status> all = Arrays.asList(values());
        public static int SIZE = all.size();
        private static final Random random = new Random();

        public static Status randomStatus() {
            return all.get(random.nextInt(SIZE));
        }
    }

}
