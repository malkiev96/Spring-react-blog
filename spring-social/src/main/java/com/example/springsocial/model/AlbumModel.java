package com.example.springsocial.model;

import com.example.springsocial.entity.Album;
import com.example.springsocial.entity.Image;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class AlbumModel extends RepresentationModel<AlbumModel> {

    private int id;
    private String title;
    private String description;
    private AuditorModel auditor;
    private CollectionModel<ImageModel> images;

    public AlbumModel(@NotNull Album album){
        this.id = album.getId();
        this.title = album.getTitle();
        this.description = album.getDescription();
        this.auditor = new AuditorModel(album);
    }
}
