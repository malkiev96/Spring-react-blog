package ru.malkiev.blog.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.malkiev.blog.entity.Auditable;
import ru.malkiev.blog.entity.User;
import ru.malkiev.blog.util.DateFormatter;

import java.util.Optional;

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
        this.lastModifiedBy = Optional.ofNullable(auditable.getLastModifiedBy())
                .map(UserModel::new)
                .orElse(null);
    }

    @Getter
    public static class UserModel {
        private final Integer id;
        private final String name;
        private final String imageUrl;

        public UserModel(@NotNull User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.imageUrl = user.getImageUrl();
        }
    }
}
