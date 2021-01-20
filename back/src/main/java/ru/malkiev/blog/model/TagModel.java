package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.Tag;

@EqualsAndHashCode(callSuper = false)
@Data
public class TagModel extends RepresentationModel<TagModel> {

    private Integer id;
    private String name;
    private String code;

    public TagModel(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        this.code = tag.getCode();
    }
}
