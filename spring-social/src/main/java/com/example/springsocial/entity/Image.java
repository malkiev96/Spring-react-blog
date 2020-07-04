package com.example.springsocial.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "images")
@EqualsAndHashCode(callSuper = true)
@Data
public class Image extends BaseFile {

    @Column(name = "height", nullable = false)
    @NotNull
    private Integer height;

    @Column(name = "width", nullable = false)
    @NotNull
    private Integer width;

}
