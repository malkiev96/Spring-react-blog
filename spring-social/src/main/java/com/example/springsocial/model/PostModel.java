package com.example.springsocial.model;

import com.example.springsocial.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class PostModel extends RepresentationModel<PostModel> {

    private int id;
    private String title;
    private String description;
    private String previewUrl;
    private boolean posted;
    private Date postedDate;
    private CategoryModel category;
    private AuditorModel auditor;

    public PostModel(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.previewUrl = post.getPreviewUrl();
        this.posted = post.getPosted();
        this.postedDate = post.getPostedDate();
        this.category = new CategoryModel(post.getCategory());
        this.auditor = new AuditorModel(post);
    }
}
