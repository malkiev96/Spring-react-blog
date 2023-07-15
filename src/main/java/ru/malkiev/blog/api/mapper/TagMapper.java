package ru.malkiev.blog.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import ru.malkiev.blog.api.model.TagModel;
import ru.malkiev.blog.domain.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

  TagModel toModel(Tag tag);

  List<TagModel> toCollectionModel(List<Tag> tags);

}
