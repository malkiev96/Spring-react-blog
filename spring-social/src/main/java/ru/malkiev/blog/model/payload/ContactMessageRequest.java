package ru.malkiev.blog.model.payload;

import lombok.Getter;
import ru.malkiev.blog.entity.ContactMessage;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class ContactMessageRequest {

    @NotEmpty
    private String name;

    @Email
    private String email;

    @NotEmpty
    private String message;

    public static ContactMessage from(ContactMessageRequest request) {
        return new ContactMessage(
                null,
                request.getName(),
                request.getEmail(),
                request.getMessage()
        );
    }
}
