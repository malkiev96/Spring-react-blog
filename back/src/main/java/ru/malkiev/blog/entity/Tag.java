package ru.malkiev.blog.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TAGS")
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false, unique = true)
    private String description;

}
