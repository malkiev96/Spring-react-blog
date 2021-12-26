package ru.malkiev.blog.assembler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.malkiev.blog.entity.ContactMessage;
import ru.malkiev.blog.model.ContactMessageModel;


@Component
@AllArgsConstructor
public class ContactMessageAssembler implements RepresentationModelAssembler<ContactMessage, ContactMessageModel> {

    private final PagedResourcesAssembler<ContactMessage> pagedAssembler;

    public PagedModel<ContactMessageModel> toPagedModel(Page<ContactMessage> page) {
        return pagedAssembler.toModel(page, this);
    }

    @Override
    public @NotNull ContactMessageModel toModel(@NotNull ContactMessage entity) {
        return new ContactMessageModel(entity);
    }

}
