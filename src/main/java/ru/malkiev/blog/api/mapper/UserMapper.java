package ru.malkiev.blog.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.malkiev.blog.api.model.UserDetailModel;
import ru.malkiev.blog.api.model.UserModel;
import ru.malkiev.blog.domain.entity.User;

@Mapper(componentModel = "spring", uses = {DateMapper.class, DocumentMapper.class})
public interface UserMapper {

  @Mapping(source = "preview.fileId", target = "preview.fileId", qualifiedByName = "encodeFileId")
  UserModel toModel(User user);

  List<UserModel> toCollectionModel(List<User> users);

  @Mapping(source = "preview.fileId", target = "preview.fileId", qualifiedByName = "encodeFileId")
  UserDetailModel toDetailModel(User user);

}
