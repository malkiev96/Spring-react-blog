package ru.malkiev.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "DOCUMENTS")
@Data
public class Document extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FILENAME", nullable = false)
    @NotNull
    private String filename;

    @Column(name = "TITLE")
    private String title;

    @Lob
    @Column(name = "BODY", nullable = false)
    private byte[] body;

    @Column(name = "DOCUMENT_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private DocumentType type;

}
