package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.Image;
import ru.malkiev.blog.util.CreateImageUrl;
import ru.malkiev.blog.util.DateFormatter;

@EqualsAndHashCode(callSuper = false)
@Data
public class ImageModel extends RepresentationModel<ImageModel> {

    private final int id;
    private final String title;
    private final String name;
    private final String type;
    private final String uploadedDate;
    private final int height;
    private final int width;
    private final String url;

    public ImageModel(Image image) {
        this.id = image.getId();
        this.title = image.getTitle();
        this.name = image.getName();
        this.type = image.getType();
        this.uploadedDate = new DateFormatter(image.getUploadedDate()).get();
        this.height = image.getHeight();
        this.width = image.getWidth();
        this.url = new CreateImageUrl(image).get();
    }
}
