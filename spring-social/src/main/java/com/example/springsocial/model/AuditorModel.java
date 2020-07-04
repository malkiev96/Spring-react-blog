package com.example.springsocial.model;

import com.example.springsocial.entity.Auditable;
import com.example.springsocial.entity.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Getter
public class AuditorModel {

    private final UserModel createdBy;
    private final UserModel lastModifiedBy;
    private final Date createdDate;
    private final Date lastModifiedDate;

    public AuditorModel(@NotNull Auditable<User> auditable) {
        this.createdBy = auditable.getCreatedBy() != null
                ? new UserModel(auditable.getCreatedBy()) : null;
        this.lastModifiedBy = auditable.getLastModifiedBy() != null
                ? new UserModel(auditable.getLastModifiedBy()) : null;
        this.createdDate = auditable.getCreatedDate();
        this.lastModifiedDate = auditable.getLastModifiedDate();
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
