package com.havlin.daniel.russian.repositories.users;

import com.havlin.daniel.russian.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.dictionary " +
            "WHERE u.username = :username")
    User findByUserNameWithDictionary(@Param("username") String username);
}
