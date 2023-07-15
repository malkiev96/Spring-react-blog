package ru.malkiev.blog.application.operation;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.api.dto.TagDto;
import ru.malkiev.blog.application.exception.TagNotFoundException;
import ru.malkiev.blog.domain.entity.Tag;
import ru.malkiev.blog.domain.repository.TagRepository;

@Component
@AllArgsConstructor
public class CreateTag implements Function<TagDto, Tag> {

  private final TagRepository repository;

  @Override
  public Tag apply(TagDto dto) {
    Tag tag = getTag(dto);
    tag.setName(dto.getName());
    tag.setCode(dto.getCode());
    return tag;
  }

  private Tag getTag(TagDto dto) {
    Integer tagId = dto.getTagId();
    return tagId == null ? new Tag() :
        repository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
  }
}
