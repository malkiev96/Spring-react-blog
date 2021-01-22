package ru.malkiev.blog.dto;

import lombok.Data;
import ru.malkiev.blog.entity.Tag;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TagDto {

    @NotNull
    @Size(min = 3, max = 16)
    protected String code;

    @NotNull
    @Size(min = 3, max = 32)
    protected String name;

    public Tag createTag() {
        Tag tag = new Tag();
        tag.setCode(code);
        tag.setName(name);
        return tag;
    }
}
