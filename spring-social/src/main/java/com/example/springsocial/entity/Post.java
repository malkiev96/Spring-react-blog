package com.example.springsocial.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post extends Auditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    @NotNull
    @Size(max = 255)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "post_text", nullable = false)
    @NotNull
    private String text;

    @Column(name = "preview_image_src")
    private String previewUrl;

    @Column(name = "posted")
    private Boolean posted = false;

    @Column(name = "date_posted")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedDate;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_files",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "id_post"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Album> albums;

}
