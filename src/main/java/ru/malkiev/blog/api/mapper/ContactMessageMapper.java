package ru.malkiev.blog.api.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.malkiev.blog.api.model.ContactMessageModel;
import ru.malkiev.blog.domain.entity.ContactMessage;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface ContactMessageMapper {

  ContactMessageModel toModel(ContactMessage message);

  default Page<ContactMessageModel> toPagedModel(Page<ContactMessage> messages) {
    return messages.map(this::toModel);
  }


}
