package ru.malkiev.springsocial.model;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.springsocial.entity.File;

import java.text.SimpleDateFormat;

@Getter
public class FileModel extends RepresentationModel<FileModel> {

    private final int id;
    private final String title;
    private final String name;
    private final String type;
    private final String uploadedDate;

    public FileModel(File file) {
        this.id = file.getId();
        this.title = file.getTitle();
        this.name = file.getName();
        this.type = file.getType();
        this.uploadedDate = new SimpleDateFormat("dd-MM-yyyy hh:mm").format(file.getUploadedDate());
    }
}
