package ru.malkiev.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.malkiev.blog.entity.Role;
import ru.malkiev.blog.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where upper(u.email) = upper(:email) ")
    Optional<User> findByEmail(@Param("email") String email);

    Boolean existsByEmail(String email);

    List<User> findAllByRole(Role role);

}
