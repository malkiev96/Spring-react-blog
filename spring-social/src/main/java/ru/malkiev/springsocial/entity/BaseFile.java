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

    @Column(name = "TITLE")
    private String title;

    @Column(name = "FILE_NAME", nullable = false)
    private String name;

    @Column(name = "FILE_PATH", nullable = false)
    private String path;

    @Column(name = "FILE_TYPE", nullable = false)
    private String type;

    @CreatedDate
    @Column(name = "UPLOADED_DATE", nullable = false)
    private Date uploadedDate;
}
