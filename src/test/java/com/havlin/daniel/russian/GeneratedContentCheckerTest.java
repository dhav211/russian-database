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

    @Test
    public void replaceLatinLetters() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("Учени'к", GeneratedContentChecker.replaceEnglishCharacters("Yчени'к")),
                () -> Assertions.assertEquals("ба'бушке", GeneratedContentChecker.replaceEnglishCharacters("бa'бушkе")),
                () -> Assertions.assertEquals("мо'ре", GeneratedContentChecker.replaceEnglishCharacters("mо're")),
                () -> Assertions.assertEquals("пе'ред", GeneratedContentChecker.replaceEnglishCharacters("пе'peд")),
                () -> Assertions.assertEquals("про'цесс", GeneratedContentChecker.replaceEnglishCharacters("про'цеcc")),
                () -> Assertions.assertEquals("ба'бушке", GeneratedContentChecker.replaceEnglishCharacters("бáбушkе")),
                () -> Assertions.assertEquals("профе'ссор", GeneratedContentChecker.replaceEnglishCharacters("профéссор"))
        );
    }

    @Test
    public void checkForMissingStress() {
        Assertions.assertAll(
                () -> Assertions.assertFalse(GeneratedContentChecker.findWordsWithMissingStress("Я пода'рил се'рому другу интере'сную кни'гу").isEmpty()),
                () -> Assertions.assertFalse(GeneratedContentChecker.findWordsWithMissingStress("Я пода'рил се'рому другу интере'сную книгу").isEmpty()),
                () -> Assertions.assertTrue(GeneratedContentChecker.findWordsWithMissingStress("Я пода'рил се'рому — другу' интере'сную кни'гу").isEmpty()),
                () -> Assertions.assertFalse(GeneratedContentChecker.findWordsWithMissingStress("Я подарил, се'рому другу' интере'сную кни'гу.").isEmpty()),
                () -> Assertions.assertTrue(GeneratedContentChecker.findWordsWithMissingStress("пода'рил").isEmpty()),
                () -> Assertions.assertFalse(GeneratedContentChecker.findWordsWithMissingStress("подарил").isEmpty()),
                () -> Assertions.assertTrue(GeneratedContentChecker.findWordsWithMissingStress("Архите'ктор управл'ял ко'мнатами так иску'сно, что каза'лось, буд'то прострa'нство танц'ует.").isEmpty())
                );
    }

    @Test
    public void fixMissingStress() {
        String text1 = "В библиоте'ке я' чита'ю толстых кни'г о' и'стории Росси'и.";
        String case1 = GeneratedContentChecker.addStressToWords(wordFormRepository,
                GeneratedContentChecker.findWordsWithMissingStress(text1),
                text1);

        String text2 = "В библиотеке я' читаю толстых кни'г о' и'стории Росси'и.";
        String case2 = GeneratedContentChecker.addStressToWords(wordFormRepository,
                GeneratedContentChecker.findWordsWithMissingStress(text2),
                text2);

        String text3 = "считающийся читаю";
        String case3 = GeneratedContentChecker.addStressToWords(wordFormRepository,
                GeneratedContentChecker.findWordsWithMissingStress(text3),
                text3);

        String text4 = "толстых библиоте'ке я' чита'ю кни'г о' и'стории Росси'и.";
        String case4 = GeneratedContentChecker.addStressToWords(wordFormRepository,
                GeneratedContentChecker.findWordsWithMissingStress(text4),
                text4);

        String text5 = "библиоте'ке я' чита'ю кни'г о' и'стории Росси'и толстых.";
        String case5 = GeneratedContentChecker.addStressToWords(wordFormRepository,
                GeneratedContentChecker.findWordsWithMissingStress(text5),
                text5);

        String text6 = "библиоте'ке я' чита'ю кни'г о' и'стории Росси'и толстых";
        String case6 = GeneratedContentChecker.addStressToWords(wordFormRepository,
                GeneratedContentChecker.findWordsWithMissingStress(text6),
                text6);

        String text7 = "толстых";
        String case7 = GeneratedContentChecker.addStressToWords(wordFormRepository,
                GeneratedContentChecker.findWordsWithMissingStress(text7),
                text7);

        Assertions.assertAll(
                () -> Assertions.assertEquals("В библиоте'ке я' чита'ю то'лстых кни'г о' и'стории Росси'и.", case1),
                () -> Assertions.assertEquals("В библиоте'ке я' чита'ю то'лстых кни'г о' и'стории Росси'и.", case2),
                () -> Assertions.assertEquals("счита'ющийся чита'ю", case3),
                () -> Assertions.assertEquals("то'лстых библиоте'ке я' чита'ю кни'г о' и'стории Росси'и.", case4),
                () -> Assertions.assertEquals("библиоте'ке я' чита'ю кни'г о' и'стории Росси'и то'лстых.", case5),
                () -> Assertions.assertEquals("библиоте'ке я' чита'ю кни'г о' и'стории Росси'и то'лстых", case6),
                () -> Assertions.assertEquals("то'лстых", case7)
        );
    }

    @Test
    public void removeRedundantStress() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("В библиоте'ке я чита'ю то'лстых книг о и'стории Росси'и.",
                        GeneratedContentChecker.removeSingleVowelStresses("В библиоте'ке я' чита'ю то'лстых кни'г о' и'стории Росси'и."))
        );
    }
}
