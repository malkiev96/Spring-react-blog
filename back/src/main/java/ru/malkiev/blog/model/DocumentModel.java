package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.Document;
import ru.malkiev.blog.entity.DocumentType;

@EqualsAndHashCode(callSuper = false)
@Data
public class DocumentModel extends RepresentationModel<DocumentModel> {

    private Long id;
    private String filename;
    private DocumentType type;
    private AuditorModel auditor;

    public DocumentModel(Document document) {
        this.id = document.getId();
        this.filename = document.getFilename();
        this.type = document.getType();
        this.auditor = new AuditorModel(document);
    }
}
