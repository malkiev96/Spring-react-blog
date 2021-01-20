package ru.malkiev.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
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

    @Column(name = "DESCRIPTION", nullable = false, length = 512)
    private String description;

    @Column(name = "POST_TEXT", nullable = false, length = 4000)
    private String text;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PREVIEW_IMG_ID")
    private Document preview;

    @Column(name = "POST_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "POST_DOCUMENTS",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "DOCUMENT_ID")
    )
    private List<Document> documents = new ArrayList<>();

}
