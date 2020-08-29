package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.Post;

@EqualsAndHashCode(callSuper = false)
@Data
public class PostModel extends RepresentationModel<PostModel> {

    private int id;
    private String title;
    private String description;
    private int viewCount;
    private boolean liked;
    private Integer likedCount;
    private Post.Status status;
    private ImageModel preview;
    private AuditorModel auditor;

    public PostModel(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.viewCount = post.getViewCount();
        this.auditor = new AuditorModel(post);
        this.status = post.getStatus();
        if (post.getPreview() != null) this.preview = new ImageModel(post.getPreview());
    }
}
