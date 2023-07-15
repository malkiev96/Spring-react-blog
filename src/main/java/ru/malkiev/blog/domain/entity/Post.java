package ru.malkiev.blog.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.malkiev.blog.domain.model.PostStatus;

@Entity
@Table(name = "POSTS")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Post extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "TITLE", nullable = false)
  @NotNull
  private String title;

  @Column(name = "DESCRIPTION", nullable = false, length = 512)
  @NotNull
  private String description;

  @Column(name = "POST_TEXT", nullable = false, length = 4000)
  @NotNull
  private String text;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "PREVIEW_IMG_ID")
  private Document preview;

  @Column(name = "POST_STATUS", nullable = false)
  @Enumerated(EnumType.STRING)
  @NotNull
  private PostStatus status;

  @Column(name = "VIEW_COUNT")
  private int viewCount;

  @ManyToOne
  @JoinColumn(name = "CATEGORY_ID", nullable = false)
  private Category category;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "POST_TAGS",
      joinColumns = @JoinColumn(name = "POST_ID"),
      inverseJoinColumns = @JoinColumn(name = "TAG_ID")
  )
  @ToString.Exclude
  private List<Tag> tags = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name = "POST_DOCUMENTS",
      joinColumns = @JoinColumn(name = "POST_ID"),
      inverseJoinColumns = @JoinColumn(name = "DOCUMENT_ID")
  )
  @ToString.Exclude
  private List<Document> documents = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Post post = (Post) o;
    return getId() != null && Objects.equals(getId(), post.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
