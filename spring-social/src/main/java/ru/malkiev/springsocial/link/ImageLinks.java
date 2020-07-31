package ru.malkiev.springsocial.link;

import org.springframework.hateoas.Link;
import ru.malkiev.springsocial.controller.ImageController;
import ru.malkiev.springsocial.entity.Image;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ImageLinks {

    public static Link linkToImage(Image entity) {
        return linkTo(methodOn(ImageController.class).getOne(entity.getId())).withRel("image");
    }
}
