package ru.malkiev.blog.link;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import ru.malkiev.blog.controller.ImageController;
import ru.malkiev.blog.entity.Image;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageLinks {

    public static Link linkToImage(Image entity) {
        return linkTo(methodOn(ImageController.class).getOne(entity.getId())).withRel("image");
    }
}
