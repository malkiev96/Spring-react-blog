package ru.malkiev.blog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.malkiev.blog.entity.Category;
import ru.malkiev.blog.entity.Tag;

@Getter
@AllArgsConstructor
public class DataItem {

    private final Integer id;
    private final String name;
    private final String description;

    public DataItem(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        this.description = tag.getDescription();
    }

    public DataItem(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
    }
}