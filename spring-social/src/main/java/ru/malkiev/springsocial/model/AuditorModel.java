package ru.malkiev.springsocial.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.malkiev.springsocial.entity.Auditable;
import ru.malkiev.springsocial.entity.User;

import java.text.SimpleDateFormat;

@Data
public class AuditorModel {

    private String createdDate;
    private String lastModifiedDate;
    private UserModel createdBy;
    private UserModel lastModifiedBy;

    public AuditorModel(@NotNull Auditable auditable) {
        this.createdBy = new UserModel(auditable.getCreatedBy());
        this.createdDate = new SimpleDateFormat("dd-MM-yyyy hh:mm").format(auditable.getCreatedDate());
        if (auditable.getLastModifiedDate() != null) {
            this.lastModifiedDate = new SimpleDateFormat("dd-MM-yyyy hh:mm")
                    .format(auditable.getLastModifiedDate());
        }
        if (auditable.getLastModifiedBy() != null) {
            this.lastModifiedBy = new UserModel(auditable.getLastModifiedBy());
        }
    }

    @Getter
    public static class UserModel {
        private final int id;
        private final String name;
        private final String imageUrl;

        public UserModel(@NotNull User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.imageUrl = user.getImageUrl();
        }
    }
}
