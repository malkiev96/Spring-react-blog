package com.example.springsocial.assembler;

import com.example.springsocial.entity.File;
import com.example.springsocial.model.FileModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class FileAssembler implements RepresentationModelAssembler<File, FileModel> {

    @Override
    public @NotNull FileModel toModel(@NotNull File entity) {
        FileModel model = new FileModel(entity);
        //todo links
        return model;
    }
}
