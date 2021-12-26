package ru.malkiev.blog.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Document;
import ru.malkiev.blog.model.DocumentModel;
import ru.malkiev.blog.service.UserService;

import static ru.malkiev.blog.link.DocumentLinks.*;

@Component
@AllArgsConstructor
public class DocumentAssembler implements RepresentationModelAssembler<Document, DocumentModel> {

    private final UserService userService;

    @Override
    public @NotNull DocumentModel toModel(@NotNull Document entity) {
        DocumentModel model = new DocumentModel(entity);
        model.add(linkToDocument(entity).withSelfRel());
        model.add(linkToDownload(entity));

        model.addIf(canDelete(entity), () -> linkToDelete(entity));

        return model;
    }

    private boolean canDelete(Document entity) {
        return userService.getCurrentUser().map(entity::canEdit).orElse(false);
    }

}
