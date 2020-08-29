package ru.malkiev.blog.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.blog.entity.ContactMessage;
import ru.malkiev.blog.util.DateFormatter;

@EqualsAndHashCode(callSuper = false)
@Data
public class ContactMessageModel extends RepresentationModel<ContactMessageModel> {

    private Integer id;
    private String name;
    private String email;
    private String message;
    private String createdDate;

    public ContactMessageModel(ContactMessage message) {
        this.id = message.getId();
        this.name = message.getName();
        this.email = message.getEmail();
        this.message = message.getMessage();
        this.createdDate = new DateFormatter(message.getCreatedDate()).get();
    }
}
