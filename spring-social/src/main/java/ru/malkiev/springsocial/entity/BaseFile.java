package ru.malkiev.springsocial.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EqualsAndHashCode
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "file_name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String path;

    @Column(name = "file_type", nullable = false)
    private String type;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_uploaded", nullable = false)
    private Date uploadedDate;
}
