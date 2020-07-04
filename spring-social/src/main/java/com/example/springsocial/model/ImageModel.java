package com.example.springsocial.model;

import com.example.springsocial.entity.Image;
import lombok.Getter;

@Getter
public class ImageModel extends BaseFileModel {

    private final int height;
    private final int width;

    public ImageModel(Image image) {
        super(image);
        this.height = image.getHeight();
        this.width = image.getWidth();
    }
}
