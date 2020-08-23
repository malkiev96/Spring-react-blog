package ru.malkiev.blog.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "USER_CREATED_ID")
    private User createdBy;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "USER_MODIFIED_ID")
    private User lastModifiedBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATE")
    private Date lastModifiedDate;

    public boolean canEdit(User user) {
        if (user == null) return false;
        if (user.getRole() == Role.ROLE_ADMIN) return true;
        return createdBy.equals(user);
    }
}
