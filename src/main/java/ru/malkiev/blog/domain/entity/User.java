package ru.malkiev.blog.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.malkiev.blog.domain.model.AuthProvider;
import ru.malkiev.blog.domain.model.Role;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "NAME", nullable = false)
  @NotNull
  private String name;

  @Email
  @NotNull
  @Column(name = "EMAIL", nullable = false, unique = true)
  private String email;

  @Column(name = "EMAIL_VERIFIED", nullable = false)
  private Boolean emailVerified = false;

  @JsonIgnore
  @NotNull
  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "PROVIDER", nullable = false)
  private AuthProvider provider;

  @NotNull
  @Column(name = "ROLE", nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "CITY")
  private String city;

  @Column(name = "ABOUT")
  private String about;

  @Column(name = "BIRTH_DATE")
  private LocalDate birthDate;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "PREVIEW_IMG_ID")
  private Document preview;

  public boolean isAdmin() {
    return role == Role.ROLE_ADMIN;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return getId() != null && Objects.equals(getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
