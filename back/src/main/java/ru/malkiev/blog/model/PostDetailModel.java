package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.malkiev.blog.entity.Post;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PostDetailModel extends PostModel {

    private Integer myStar;
    private Double rating;
    private CategoryModel category;
    private String text;
    private List<TagModel> tags = new ArrayList<>();
    private List<DocumentModel> documents = new ArrayList<>();

    public PostDetailModel(Post post) {
        super(post);
        this.text = post.getText();
        this.category = new CategoryModel(post.getCategory());
        post.getTags().forEach(tag -> this.tags.add(new TagModel(tag)));
        post.getDocuments().forEach(doc -> this.documents.add(new DocumentModel(doc)));
    }
}
