package ru.malkiev.springsocial.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.CollectionModel;
import ru.malkiev.springsocial.entity.Post;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostDetailModel extends PostModel {

    private String text;
    private List<DataItem> tags = new ArrayList<>();
    private CollectionModel<FileModel> files;
    private CollectionModel<ImageModel> images;

    public PostDetailModel(Post post) {
        super(post);
        this.text = post.getText();
        post.getTags().forEach(tag -> {
            this.tags.add(new DataItem(tag.getId(), tag.getName(), tag.getDescription()));
        });
    }
}
