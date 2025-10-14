package com.havlin.daniel.russian;

import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.utils.GeneratedContentChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeneratedContentCheckerTest {
    @Autowired
    private WordFormRepository wordFormRepository;

    @Test
    public void checkForStress() {
        String cobaka = "соба́ка";
        String dom = "дом";
        String collection = "Коли́чество";
        String wordWithSingleQuote = "ко'мнат";
        String wholeSentence = "Коли́чество ко'мнат в кварти'ре зави'сит от её площа'ди.";
        String oddStress = "Архите'ктор управл'ял ко'мнатами так иску'сно, что каза'лось, буд'то прострáнство танц'ует.";

        Assertions.assertAll(
                () -> Assertions.assertTrue(GeneratedContentChecker.doesSentenceContainLettersWithStressMarks(cobaka)),
                () -> Assertions.assertFalse(GeneratedContentChecker.doesSentenceContainLettersWithStressMarks(dom)),
                () -> Assertions.assertTrue(GeneratedContentChecker.doesSentenceContainLettersWithStressMarks(collection)),
                () -> Assertions.assertFalse(GeneratedContentChecker.doesSentenceContainLettersWithStressMarks(wordWithSingleQuote)),
                () -> Assertions.assertTrue(GeneratedContentChecker.doesSentenceContainLettersWithStressMarks(wholeSentence)),
                () -> Assertions.assertTrue(GeneratedContentChecker.doesSentenceContainLettersWithStressMarks(oddStress))

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
        String oddStress = "Архите'ктор управл'ял ко'мнатами так иску'сно, что каза'лось, буд'то прострáнство танц'ует.";
        String anotherOddStress = "Де'ти óстались игра'ть во дворе'.";

        Assertions.assertAll(
                () -> Assertions.assertTrue(GeneratedContentChecker.doesSentenceContainLatinLetters(containsLatinLetters)),
                () -> Assertions.assertTrue(GeneratedContentChecker.doesSentenceContainLatinLetters(latinC)),
                () -> Assertions.assertFalse(GeneratedContentChecker.doesSentenceContainLatinLetters(russianC)),
                () -> Assertions.assertFalse(GeneratedContentChecker.doesSentenceContainLatinLetters(noLatinLetters)),
                () -> Assertions.assertTrue(GeneratedContentChecker.doesSentenceContainLatinLetters(fullSentenceWithLatin)),
                () -> Assertions.assertFalse(GeneratedContentChecker.doesSentenceContainLatinLetters(fullSentenceNoLatin)),
                () -> Assertions.assertFalse(GeneratedContentChecker.doesSentenceContainLatinLetters(oddStress)),
                () -> Assertions.assertFalse(GeneratedContentChecker.doesSentenceContainLatinLetters(anotherOddStress))
        );
    }

//    @Test
//    public void replaceLatinLetters() {
//        Assertions.assertAll(
//                () -> Assertions.assertEquals("Учени'к", GeneratedContentChecker.replaceEnglishCharacters("Yчени'к")),
//                () -> Assertions.assertEquals("ба'бушке", GeneratedContentChecker.replaceEnglishCharacters("бa'бушkе")),
//                () -> Assertions.assertEquals("мо'ре", GeneratedContentChecker.replaceEnglishCharacters("mо're")),
//                () -> Assertions.assertEquals("пе'ред", GeneratedContentChecker.replaceEnglishCharacters("пе'peд")),
//                () -> Assertions.assertEquals("про'цесс", GeneratedContentChecker.replaceEnglishCharacters("про'цеcc")),
//                () -> Assertions.assertEquals("ба'бушке", GeneratedContentChecker.replaceEnglishCharacters("бáбушkе")),
//                () -> Assertions.assertEquals("профе'ссор", GeneratedContentChecker.replaceEnglishCharacters("профéссор"))
//        );
//    }
}
