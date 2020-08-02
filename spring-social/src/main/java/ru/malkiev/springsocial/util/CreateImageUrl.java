package ru.malkiev.springsocial.util;

import lombok.AllArgsConstructor;
import ru.malkiev.springsocial.controller.ImageController;
import ru.malkiev.springsocial.entity.Image;

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
