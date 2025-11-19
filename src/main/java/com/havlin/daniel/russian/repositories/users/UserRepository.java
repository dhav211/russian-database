package com.havlin.daniel.russian.repositories.users;

import com.havlin.daniel.russian.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
