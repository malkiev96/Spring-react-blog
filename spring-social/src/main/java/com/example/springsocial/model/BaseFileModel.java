package com.example.springsocial.model;

import com.example.springsocial.entity.BaseFile;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Getter
public abstract class BaseFileModel extends RepresentationModel<BaseFileModel> {

    private final int id;
    private final String title;
    private final String name;
    private final String path;
    private final String type;
    private final Date uploadedDate;

    public BaseFileModel(BaseFile file){
        this.id = file.getId();
        this.title = file.getTitle();
        this.name = file.getName();
        this.path = file.getPath();
        this.type = file.getType();
        //todo format date
        this.uploadedDate = file.getUploadedDate();
    }
}
