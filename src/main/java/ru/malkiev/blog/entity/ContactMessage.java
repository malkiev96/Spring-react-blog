package ru.malkiev.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "CONTACT_MESSAGES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    @NotEmpty
    private String name;

    @Column(name = "EMAIL", nullable = false)
    @Email
    private String email;

    @Column(name = "MESSAGE", nullable = false)
    @NotEmpty
    private String message;

    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;
}
