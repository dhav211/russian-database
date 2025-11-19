package com.havlin.daniel.russian.services;

import com.havlin.daniel.russian.entities.users.User;
import com.havlin.daniel.russian.entities.users.UserRole;
import com.havlin.daniel.russian.services.users.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTests {
    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void createsUser() {
        User user = userService.createUser("dhav211", "password", "dhav211@gmail.com", UserRole.ADMIN);

        Assertions.assertNotEquals("password", user.getPassword());
    }
}
