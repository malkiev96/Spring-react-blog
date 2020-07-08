package ru.malkiev.springsocial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.springsocial.entity.Category;
import ru.malkiev.springsocial.entity.Post;

import java.text.SimpleDateFormat;

@EqualsAndHashCode(callSuper = false)
@Data
public class PostModel extends RepresentationModel<PostModel> {

    private int id;
    private String title;
    private String description;
    private ImageModel preview;
    private boolean posted;
    private String postedDate;
    private DataItem category;
    private AuditorModel auditor;

    public PostModel(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.posted = post.getPosted();
        this.auditor = new AuditorModel(post);
        Category category = post.getCategory();
        this.category = new DataItem(category.getId(), category.getName(), category.getDescription());
        if (posted) {
            this.postedDate = new SimpleDateFormat("dd-MM-yyyy hh:mm")
                    .format(post.getPostedDate());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class DataItem {
        private final int id;
        private final String name;
        private final String description;
    }
}
