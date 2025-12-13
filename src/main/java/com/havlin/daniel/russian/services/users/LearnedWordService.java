package com.havlin.daniel.russian.services.users;

import com.havlin.daniel.russian.entities.users.LearnedWord;
import com.havlin.daniel.russian.entities.users.User;
import com.havlin.daniel.russian.repositories.users.LearnedWordRepository;
import com.havlin.daniel.russian.repositories.users.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class LearnedWordService {
    private static final Logger log = LoggerFactory.getLogger(LearnedWordService.class);
    private final LearnedWordRepository learnedWordRepository;
    private final UserRepository userRepository;

    public LearnedWordService(LearnedWordRepository learnedWordRepository, UserRepository userRepository) {
        this.learnedWordRepository = learnedWordRepository;
        this.userRepository = userRepository;
    }

    /**
     * Add a new learned word to a user's dictionary.
     * @param user The user who will receive new learned word
     * @param wordId The id of the word will the learned word will be based on
     * @return The newly created and saved learned word
     */
    @Transactional
    public LearnedWord createLearnedWord(User user, Long wordId) {
        try {
            LearnedWord learnedWord = new LearnedWord();
            learnedWord.setUser(user);
            learnedWord.setWordId(wordId);
            learnedWord.setLearnedDate();

            user.getDictionary().add(learnedWord);

            learnedWordRepository.save(learnedWord);
            userRepository.save(user);

            return learnedWord;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Remove the learned word from the user's dictionary.
     * @param user The user whose dictionary the learned word will be removed from
     * @param learnedWord The learned word removed
     */
    public void removeLearnedWord(User user, LearnedWord learnedWord) {
        try {
            learnedWordRepository.delete(learnedWord);
            user.getDictionary().remove(learnedWord);
        } catch (Exception e) { // null value was probably attempted to be used up above
            log.error(e.getMessage());
        }
    }

    /**
     * Checks to see if the user has added the word to their dictionary.
     * @param userId User id of the user
     * @param wordId Word id of the word we are searching for
     * @return Boolean value whether user has learnt the word before
     */
    public boolean doesUserHaveWordInDictionary(Long userId, Long wordId) {
        Optional<LearnedWord> foundWord = learnedWordRepository.findLearnedWordByUserAndWordId(userId, wordId);
        return foundWord.isPresent();
    }

    /**
     * Adjust score and set other details that will change after the user completes an exercise
     * @param learnedWord
     * @param wasCorrect
     */
    public void learnedWordExerciseCompleted(LearnedWord learnedWord, boolean wasCorrect) {
        if (learnedWord != null) {
            int currentScore = learnedWord.getScore();
            learnedWord.setScore(wasCorrect ? currentScore + 1 : currentScore - 1);
            learnedWord.setTimesPracticed();
            learnedWord.setLastSeenDate();
        }
    }
}
