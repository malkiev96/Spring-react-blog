package ru.malkiev.springsocial.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IMAGES")
@EqualsAndHashCode(callSuper = true)
@Data
public class Image extends BaseFile {

    @Column(name = "HEIGHT", nullable = false)
    private Integer height;

    @Column(name = "WIDTH", nullable = false)
    private Integer width;

    @ManyToMany(mappedBy = "images")
    private List<Post> posts = new ArrayList<>();

}
