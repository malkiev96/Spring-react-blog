package ru.malkiev.springsocial.entity;

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
    @JoinColumn(name = "user_created_id")
    private User createdBy;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "user_updated_id")
    private User lastModifiedBy;

    @CreatedDate
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated")
    private Date lastModifiedDate;

    public boolean canEdit(User user){
        if (user == null) {
            return false;
        }
        if (user.getRole()==Role.ROLE_ADMIN){
            return true;
        }
        return createdBy.equals(user);
    }
}
