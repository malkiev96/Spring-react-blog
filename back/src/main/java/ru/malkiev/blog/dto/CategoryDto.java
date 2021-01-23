package ru.malkiev.blog.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryDto extends TagDto {

    private Integer categoryId;

    private Integer parentId;
}
