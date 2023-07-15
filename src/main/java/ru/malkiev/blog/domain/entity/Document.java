package ru.malkiev.blog.domain.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.malkiev.blog.domain.model.DocumentType;

@Entity
@Table(name = "DOCUMENTS")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Document extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "FILENAME", nullable = false)
  @NotNull
  private String filename;

  @Lob
  @Column(name = "BODY", nullable = false)
  @NotNull
  private byte[] body;

  @Column(name = "DOCUMENT_TYPE", nullable = false)
  @Enumerated(EnumType.STRING)
  @NotNull
  private DocumentType type;

  @Column(name = "STORE_PATH")
  @NotNull
  private String fileId;

  @Column(name = "FILE_SIZE")
  @NotNull
  private Long fileSize;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Document document = (Document) o;
    return getId() != null && Objects.equals(getId(), document.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
