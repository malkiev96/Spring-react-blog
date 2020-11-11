package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.malkiev.blog.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PostDetailModel extends PostModel {

    private Integer myStar;
    private Double rating;
    private DataItem category;
    private String text;
    private List<DataItem> tags = new ArrayList<>();
    private List<ImageModel> images = new ArrayList<>();

    public PostDetailModel(Post post) {
        super(post);
        this.text = post.getText();
        this.category = new DataItem(post.getCategory());
        post.getTags().forEach(tag -> this.tags.add(new DataItem(tag)));
        this.tags.addAll(post.getTags().stream().map(DataItem::new).collect(Collectors.toList()));
        this.images.addAll(post.getImages().stream().map(ImageModel::new).collect(Collectors.toList()));
    }
}
