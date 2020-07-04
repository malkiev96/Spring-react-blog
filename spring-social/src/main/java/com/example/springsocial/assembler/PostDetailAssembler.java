package com.example.springsocial.assembler;

import com.example.springsocial.entity.Post;
import com.example.springsocial.model.PostDetailModel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostDetailAssembler implements RepresentationModelAssembler<Post, PostDetailModel> {

    private final AlbumAssembler albumAssembler;
    private final FileAssembler fileAssembler;

    @Override
    public @NotNull PostDetailModel toModel(@NotNull Post entity) {
        PostDetailModel model = new PostDetailModel(entity);
        model.setAlbums(albumAssembler.toCollectionModel(entity.getAlbums()));
        model.setFiles(fileAssembler.toCollectionModel(entity.getFiles()));

        //todo links

        return model;
    }
}
