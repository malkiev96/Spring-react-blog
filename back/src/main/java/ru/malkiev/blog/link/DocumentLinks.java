package ru.malkiev.blog.link;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import ru.malkiev.blog.controller.DocumentController;
import ru.malkiev.blog.entity.Document;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentLinks {

    public static Link linkToDocument(Document entity) {
        return linkTo(methodOn(DocumentController.class).getOne(entity.getId())).withRel("document");
    }

    public static Link linkToDownload(Document entity) {
        return linkTo(methodOn(DocumentController.class).download(entity.getId())).withRel("download");
    }

    public static Link linkToDelete(Document entity) {
        return linkTo(methodOn(DocumentController.class).delete(entity.getId())).withRel("deleteDocument");
    }
}
