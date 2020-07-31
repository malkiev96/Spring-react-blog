package ru.malkiev.springsocial.operation;

import ru.malkiev.springsocial.controller.ImageController;
import ru.malkiev.springsocial.entity.Image;

import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CreateImageUrl implements Function<Image, String> {

    @Override
    public String apply(Image image) {
        return linkTo(methodOn(ImageController.class)
                .getFile(image.getName(), null))
                .toUri().toString();
    }
}
