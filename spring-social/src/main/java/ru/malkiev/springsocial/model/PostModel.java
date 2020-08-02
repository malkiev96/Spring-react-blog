package ru.malkiev.springsocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.springsocial.entity.Category;
import ru.malkiev.springsocial.entity.Post;

@EqualsAndHashCode(callSuper = false)
@Data
public class PostModel extends RepresentationModel<PostModel> {

    private int id;
    private String title;
    private String description;
    private int viewCount;
    private boolean liked;
    private Integer myStar;
    private Double rating;
    private DataItem category;
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

        Category category = post.getCategory();
        this.category = new DataItem(category.getId(), category.getName(), category.getDescription());
    }

    @Getter
    @AllArgsConstructor
    public static class DataItem {
        private final int id;
        private final String name;
        private final String description;
    }
}
