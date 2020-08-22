package ru.malkiev.springsocial.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "IMAGES")
@EntityListeners(AuditingEntityListener.class)
public class Image {

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

    @Column(name = "HEIGHT", nullable = false)
    private Integer height;

    @Column(name = "WIDTH", nullable = false)
    private Integer width;

    @ManyToMany(mappedBy = "images")
    private List<Post> posts = new ArrayList<>();

}
