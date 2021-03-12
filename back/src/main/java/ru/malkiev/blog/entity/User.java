package ru.malkiev.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "USERS")
@Data
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

    @Column(name = "IMAGE_URL")
    private String imageUrl;

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

    public boolean isAdmin(){
        return Role.ROLE_ADMIN.equals(role);
    }

}
