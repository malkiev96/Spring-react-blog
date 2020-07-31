package ru.malkiev.springsocial.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.malkiev.springsocial.entity.Auditable;
import ru.malkiev.springsocial.entity.User;
import ru.malkiev.springsocial.util.DateFormatter;

@Data
public class AuditorModel {

    private String createdDate;
    private String lastModifiedDate;
    private UserModel createdBy;
    private UserModel lastModifiedBy;

    public AuditorModel(@NotNull Auditable auditable) {
        this.createdBy = new UserModel(auditable.getCreatedBy());
        this.createdDate = new DateFormatter(auditable.getCreatedDate()).get();
        this.lastModifiedDate = new DateFormatter(auditable.getLastModifiedDate()).get();
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
