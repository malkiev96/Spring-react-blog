package ru.malkiev.blog.util;

import lombok.AllArgsConstructor;
import ru.malkiev.blog.controller.ImageController;
import ru.malkiev.blog.entity.Image;

import java.util.function.Supplier;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
public class CreateImageUrl implements Supplier<String> {

    private final Image image;

    @Override
    public String get() {
        return linkTo(methodOn(ImageController.class)
                .getFile(image.getName(), null))
                .toUri().toString();
    }
}
