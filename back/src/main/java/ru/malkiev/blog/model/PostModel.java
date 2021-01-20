package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.Post;
import ru.malkiev.blog.entity.PostStatus;

import java.util.Optional;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class PostModel extends RepresentationModel<PostModel> {

    private Integer id;
    private String title;
    private String description;
    private int viewCount;
    private boolean liked;
    private Integer likedCount;
    private PostStatus status;
    private DocumentModel preview;
    private AuditorModel auditor;

    public PostModel(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.viewCount = post.getViewCount();
        this.auditor = new AuditorModel(post);
        this.status = post.getStatus();
        this.preview = Optional.ofNullable(post.getPreview())
                .map(DocumentModel::new).orElse(null);
    }
}
