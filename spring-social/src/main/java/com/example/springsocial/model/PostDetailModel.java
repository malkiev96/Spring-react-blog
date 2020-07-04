package com.example.springsocial.model;

import com.example.springsocial.entity.Post;
import com.example.springsocial.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostDetailModel extends PostModel {

    private String text;
    private List<Tag> tags;
    private CollectionModel<FileModel> files;
    private CollectionModel<AlbumModel> albums;

    public PostDetailModel(Post post) {
        super(post);
        this.text = post.getText();
        this.tags = post.getTags();
    }
}
