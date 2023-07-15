package ru.malkiev.blog.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.blog.domain.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

  List<Category> findAllByParentIsNull();
}
