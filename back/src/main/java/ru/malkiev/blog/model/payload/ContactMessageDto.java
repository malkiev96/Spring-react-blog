package ru.malkiev.blog.model.payload;

import lombok.Getter;
import ru.malkiev.blog.entity.ContactMessage;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class ContactMessageDto {

    @NotEmpty
    private String name;

    @Email
    private String email;

    @NotEmpty
    private String message;

    public static ContactMessage from(ContactMessageDto dto) {
        return new ContactMessage(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getMessage()
        );
    }
}
