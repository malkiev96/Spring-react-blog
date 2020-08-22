package ru.malkiev.springsocial.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import ru.malkiev.springsocial.entity.ContactMessage;

@EqualsAndHashCode(callSuper = false)
@Data
public class ContactMessageModel extends RepresentationModel<ContactMessageModel> {

    private Integer id;
    private String name;
    private String email;
    private String message;

    public ContactMessageModel(ContactMessage message) {
        this.id = message.getId();
        this.name = message.getName();
        this.email = message.getEmail();
        this.message = message.getMessage();
    }
}
