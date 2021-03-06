package ru.malkiev.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostDto {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String text;

    private boolean posted;

    private Long previewId;

    @NotNull
    private Integer categoryId;

    @NotEmpty
    private List<Integer> tagIds = new ArrayList<>();

    private List<Long> documentIds = new ArrayList<>();

}
