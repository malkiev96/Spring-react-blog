package ru.malkiev.springsocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malkiev.springsocial.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
}
