package ru.malkiev.blog.api.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

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

  private String previewId;

  @NotNull
  private Integer categoryId;

  @NotEmpty
  private List<Integer> tagIds = new ArrayList<>();

  private List<String> documentIds = new ArrayList<>();

}
