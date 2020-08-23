package ru.malkiev.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORIES")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME", nullable = false, unique = true)
    @Size(min = 3, max = 255)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @NotFound(action = NotFoundAction.IGNORE)
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> childs = new ArrayList<>();
}
