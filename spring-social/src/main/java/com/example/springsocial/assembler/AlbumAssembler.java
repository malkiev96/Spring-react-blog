package com.example.springsocial.assembler;

import com.example.springsocial.entity.Album;
import com.example.springsocial.model.AlbumModel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AlbumAssembler implements RepresentationModelAssembler<Album, AlbumModel> {

    private final ImageAssembler imageAssembler;

    @Override
    public @NotNull AlbumModel toModel(@NotNull Album entity) {
        AlbumModel model = new AlbumModel(entity);
        model.setImages(imageAssembler.toCollectionModel(entity.getImages()));

        return model;
    }
}
