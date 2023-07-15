package ru.malkiev.blog.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

  @CreatedBy
  @ManyToOne
  @JoinColumn(name = "USER_CREATED_ID", nullable = false)
  private User createdBy;

  @LastModifiedBy
  @ManyToOne
  @JoinColumn(name = "USER_MODIFIED_ID")
  private User lastModifiedBy;

  @CreatedDate
  @Column(name = "CREATED_DATE", nullable = false)
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name = "MODIFIED_DATE")
  private LocalDateTime lastModifiedDate;

}
