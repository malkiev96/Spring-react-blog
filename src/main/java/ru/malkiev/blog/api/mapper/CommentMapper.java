package ru.malkiev.blog.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.malkiev.blog.api.model.CommentModel;
import ru.malkiev.blog.domain.entity.Comment;

@Mapper(componentModel = "spring", uses = {DateMapper.class, DocumentMapper.class})
public interface CommentMapper {

  @Mapping(source = "createdBy.preview.fileId", target = "createdBy.preview.fileId", qualifiedByName = "encodeFileId")
  @Mapping(source = "lastModifiedBy.preview.fileId", target = "lastModifiedBy.preview.fileId", qualifiedByName = "encodeFileId")
  CommentModel toModel(Comment comment);

  List<CommentModel> toCollectionModel(List<Comment> comments);

}
