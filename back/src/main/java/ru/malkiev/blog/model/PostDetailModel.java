package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.malkiev.blog.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostDetailModel extends PostModel {

    private String text;
    private List<DataItem> tags = new ArrayList<>();
    private List<ImageModel> images = new ArrayList<>();

    public PostDetailModel(Post post) {
        super(post);
        this.text = post.getText();
        post.getTags().forEach(tag -> {
            this.tags.add(new DataItem(tag.getId(), tag.getName(), tag.getDescription()));
        });
        this.images.addAll(post.getImages().stream().map(ImageModel::new).collect(Collectors.toList()));
    }
}
