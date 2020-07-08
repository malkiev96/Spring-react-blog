package ru.malkiev.springsocial.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PostRequest {

    @NotNull
    @Size(max = 255)
    private String title;
    private String description;
    @NotNull
    private String text;
    private boolean posted;
    private Integer previewId;
    @NotNull
    private Integer categoryId;
    @NotNull
    private List<Integer> tagIds;
    private List<Integer> fileIds;
    private List<Integer> imageIds;

}
