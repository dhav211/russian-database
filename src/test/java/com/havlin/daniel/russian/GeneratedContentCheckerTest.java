package com.havlin.daniel.russian;

import com.havlin.daniel.russian.utils.GeneratedContentChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeneratedContentCheckerTest {
    @Test
    public void checkForStress() {
        String cobaka = "соба́ка";
        String dom = "дом";
        String collection = "Коли́чество";
        String wordWithSingleQuote = "ко'мнат";
        String wholeSentence = "Коли́чество ко'мнат в кварти'ре зави'сит от её площа'ди.";

        Assertions.assertAll(
                () -> Assertions.assertTrue(GeneratedContentChecker.sentenceContainsLettersWithStressMarks(cobaka)),
                () -> Assertions.assertFalse(GeneratedContentChecker.sentenceContainsLettersWithStressMarks(dom)),
                () -> Assertions.assertTrue(GeneratedContentChecker.sentenceContainsLettersWithStressMarks(collection)),
                () -> Assertions.assertFalse(GeneratedContentChecker.sentenceContainsLettersWithStressMarks(wordWithSingleQuote)),
                () -> Assertions.assertTrue(GeneratedContentChecker.sentenceContainsLettersWithStressMarks(wholeSentence))
        );
    }

    @Test
    public void checkForLatinLetters() {
        String containsLatinLetters = "Yчени'к";
        String latinC = "c";
        String russianC = "с";
        String noLatinLetters = "помога'ть";
        String fullSentenceWithLatin = "Я оста'нусь помога'ть ба'бушkе в саду'.";
        String fullSentenceNoLatin = "Де'ти оста'лись игра'ть во дворе'.";

        Assertions.assertAll(
                () -> Assertions.assertTrue(GeneratedContentChecker.sentenceContainsLatinLetters(containsLatinLetters)),
                () -> Assertions.assertTrue(GeneratedContentChecker.sentenceContainsLatinLetters(latinC)),
                () -> Assertions.assertFalse(GeneratedContentChecker.sentenceContainsLatinLetters(russianC)),
                () -> Assertions.assertFalse(GeneratedContentChecker.sentenceContainsLatinLetters(noLatinLetters)),
                () -> Assertions.assertTrue(GeneratedContentChecker.sentenceContainsLatinLetters(fullSentenceWithLatin)),
                () -> Assertions.assertFalse(GeneratedContentChecker.sentenceContainsLatinLetters(fullSentenceNoLatin))
        );
    }

    @Test
    public void replaceLatinLetters() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("Учени'к", GeneratedContentChecker.replaceEnglishCharacters("Yчени'к")),
                () -> Assertions.assertEquals("ба'бушке", GeneratedContentChecker.replaceEnglishCharacters("бa'бушkе")),
                () -> Assertions.assertEquals("мо'ре", GeneratedContentChecker.replaceEnglishCharacters("mо're")),
                () -> Assertions.assertEquals("пе'ред", GeneratedContentChecker.replaceEnglishCharacters("пе'peд")),
                () -> Assertions.assertEquals("про'цесс", GeneratedContentChecker.replaceEnglishCharacters("про'цеcc"))
        );
    }
}
