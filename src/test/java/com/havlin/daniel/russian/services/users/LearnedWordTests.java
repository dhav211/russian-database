package com.havlin.daniel.russian.services.users;

import com.havlin.daniel.russian.entities.users.LearnedWord;
import com.havlin.daniel.russian.entities.users.SecurityUser;
import com.havlin.daniel.russian.entities.users.User;
import com.havlin.daniel.russian.repositories.users.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
public class LearnedWordTests {
    @Autowired
    private LearnedWordService learnedWordService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void createLearnedWord() {
        User user = userRepository.findByUsername("test_man");
        LearnedWord learnedWord = learnedWordService.createLearnedWord(user, 233L);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, user.getDictionary().size())
        );
    }

    @Test
    @Transactional
    public void doesHaveLearnedWord() {
        User user = userRepository.findByUsername("test_man");
        LearnedWord learnedWord = learnedWordService.createLearnedWord(user, 233L);

        Assertions.assertAll(
                () -> Assertions.assertTrue(learnedWordService.doesUserHaveWordInDictionary(user.getId(), 233L)),
                () -> Assertions.assertFalse(learnedWordService.doesUserHaveWordInDictionary(user.getId(), 548L))
        );
    }

    @Test
    @Transactional
    public void removeLearnedWord() {
        User user = userRepository.findByUsername("test_man");
        LearnedWord learnedWord = learnedWordService.createLearnedWord(user, 233L);
        learnedWordService.removeLearnedWord(user, learnedWord);
        learnedWordService.removeLearnedWord(user, null);

        Assertions.assertTrue(user.getDictionary().isEmpty());
    }
}
