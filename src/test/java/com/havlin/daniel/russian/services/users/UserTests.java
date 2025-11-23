package com.havlin.daniel.russian.services.users;

import com.havlin.daniel.russian.entities.users.User;
import com.havlin.daniel.russian.entities.users.UserRole;
import jakarta.transaction.Transactional;
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
        User user = userService.createUser("test_man", "password123", "testman@gmail.com", UserRole.BASIC);
//
//        Assertions.assertNotEquals("password", user.getPassword());
    }
}
