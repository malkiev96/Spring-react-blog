package com.example.springsocial.model;

import com.example.springsocial.entity.Tag;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class TagModel extends RepresentationModel<TagModel> {

    private final int id;
    private final String name;
    private final String description;

    public TagModel(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        this.description = tag.getDescription();
    }
}
