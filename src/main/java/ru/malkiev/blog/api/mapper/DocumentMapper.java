package ru.malkiev.blog.api.mapper;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.util.UriUtils;
import ru.malkiev.blog.api.model.DocumentModel;
import ru.malkiev.blog.domain.entity.Document;
import ru.malkiev.blog.domain.model.DocumentType;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface DocumentMapper {

  @Mapping(source = "fileId", target = "fileId", qualifiedByName = "encodeFileId")
  @Mapping(source = "document", target = "isImage", qualifiedByName = "isImage")
  DocumentModel toModel(Document document);

  @Named("encodeFileId")
  static String encodeFileId(String fileId) {
    return fileId == null ? null : UriUtils.encode(fileId, StandardCharsets.UTF_8);
  }

  @Named("isImage")
  static Boolean isImage(Document document) {
    return Optional.ofNullable(document)
        .map(Document::getType)
        .map(DocumentType::isImage)
        .orElse(null);
  }

}
