package ru.malkiev.blog.domain.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(name = "TAGS")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "NAME", nullable = false, unique = true)
  @NotNull
  private String name;

  @Column(name = "CODE", nullable = false, unique = true)
  @NotNull
  private String code;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Tag tag = (Tag) o;
    return getId() != null && Objects.equals(getId(), tag.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
