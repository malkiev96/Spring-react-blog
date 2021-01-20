package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import ru.malkiev.blog.entity.Post;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PostDetailModel extends PostModel {

    private Integer myStar;
    private Double rating;
    private CategoryModel category;
    private String text;
    private CollectionModel<TagModel> tags;
    private CollectionModel<DocumentModel> documents;

    public PostDetailModel(Post post) {
        super(post);
        this.text = post.getText();
        this.category = new CategoryModel(post.getCategory());
    }
}
