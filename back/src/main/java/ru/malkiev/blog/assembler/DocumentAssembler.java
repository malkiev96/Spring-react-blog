package ru.malkiev.blog.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.Document;
import ru.malkiev.blog.model.DocumentModel;

import static ru.malkiev.blog.link.DocumentLinks.linkToDocument;
import static ru.malkiev.blog.link.DocumentLinks.linkToDownload;

@Component
@AllArgsConstructor
public class DocumentAssembler implements RepresentationModelAssembler<Document, DocumentModel> {

    private final PagedResourcesAssembler<Document> pagedAssembler;

    public PagedModel<DocumentModel> toPagedModel(Page<Document> page) {
        return pagedAssembler.toModel(page, this);
    }

    @Override
    public @NotNull DocumentModel toModel(@NotNull Document entity) {
        DocumentModel model = new DocumentModel(entity);
        model.add(linkToDocument(entity).withSelfRel());
        model.add(linkToDownload(entity));

        return model;
    }

}
