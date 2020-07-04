package com.example.springsocial.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@MappedSuperclass
@EqualsAndHashCode
@Data
public abstract class BaseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    @Size(max = 255)
    private String title;

    @Column(name = "file_name", nullable = false)
    @NotNull
    @Size(max = 255)
    private String name;

    @Column(name = "file_path", nullable = false)
    @NotNull
    @Size(max = 255)
    private String path;

    @Column(name = "file_type", nullable = false)
    @NotNull
    @Size(max = 128)
    private String type;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_uploaded", nullable = false)
    private Date uploadedDate;
}
