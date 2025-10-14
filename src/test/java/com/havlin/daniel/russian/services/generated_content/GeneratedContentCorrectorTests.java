package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GeneratedContentCorrectorTests {
    @Autowired
    WordFormRepository wordFormRepository;

    @Test
    public void checkForMissingStress() {
        GeneratedContentCorrector generatedContentCorrector = new GeneratedContentCorrector(wordFormRepository);
        Assertions.assertAll(
                () -> Assertions.assertFalse(generatedContentCorrector.findWordsWithMissingStress("Я пода'рил се'рому другу интере'сную кни'гу").isEmpty()),
                () -> Assertions.assertFalse(generatedContentCorrector.findWordsWithMissingStress("Я пода'рил се'рому другу интере'сную книгу").isEmpty()),
                () -> Assertions.assertTrue(generatedContentCorrector.findWordsWithMissingStress("Я пода'рил се'рому — другу' интере'сную кни'гу").isEmpty()),
                () -> Assertions.assertFalse(generatedContentCorrector.findWordsWithMissingStress("Я подарил, се'рому другу' интере'сную кни'гу.").isEmpty()),
                () -> Assertions.assertTrue(generatedContentCorrector.findWordsWithMissingStress("пода'рил").isEmpty()),
                () -> Assertions.assertFalse(generatedContentCorrector.findWordsWithMissingStress("подарил").isEmpty()),
                () -> Assertions.assertTrue(generatedContentCorrector.findWordsWithMissingStress("Архите'ктор управл'ял ко'мнатами так иску'сно, что каза'лось, буд'то прострa'нство танц'ует.").isEmpty())
        );
    }

    @Test
    public void fixMissingStress() {
        GeneratedContentCorrector generatedContentCorrector = new GeneratedContentCorrector(wordFormRepository);

        String text1 = "В библиоте'ке я' чита'ю толстых кни'г о' и'стории Росси'и.";
        List<String> missingStress1 = generatedContentCorrector.findWordsWithMissingStress(text1);
        String case1 = generatedContentCorrector
                .addStressToWords(generatedContentCorrector.getUnstressedCorrections(missingStress1), text1);

        String text2 = "В библиотеке я' читаю толстых кни'г о' и'стории Росси'и.";
        List<String> missingStress2 = generatedContentCorrector.findWordsWithMissingStress(text2);
        String case2 = generatedContentCorrector
                .addStressToWords(generatedContentCorrector.getUnstressedCorrections(missingStress2), text2);

        String text3 = "считающийся читаю";
        List<String> missingStress3 = generatedContentCorrector.findWordsWithMissingStress(text3);
        String case3 = generatedContentCorrector
                .addStressToWords(generatedContentCorrector.getUnstressedCorrections(missingStress3), text3);

        String text4 = "толстых библиоте'ке я' чита'ю кни'г о' и'стории Росси'и.";
        List<String> missingStress4 = generatedContentCorrector.findWordsWithMissingStress(text4);
        String case4 = generatedContentCorrector
                .addStressToWords(generatedContentCorrector.getUnstressedCorrections(missingStress4), text4);

        String text5 = "библиоте'ке я' чита'ю кни'г о' и'стории Росси'и толстых.";
        List<String> missingStress5 = generatedContentCorrector.findWordsWithMissingStress(text5);
        String case5 = generatedContentCorrector
                .addStressToWords(generatedContentCorrector.getUnstressedCorrections(missingStress5), text5);

        String text6 = "библиоте'ке я' чита'ю кни'г о' и'стории Росси'и толстых";
        List<String> missingStress6 = generatedContentCorrector.findWordsWithMissingStress(text6);
        String case6 = generatedContentCorrector
                .addStressToWords(generatedContentCorrector.getUnstressedCorrections(missingStress6), text6);

        String text7 = "толстых";
        List<String> missingStress7 = generatedContentCorrector.findWordsWithMissingStress(text7);
        String case7 = generatedContentCorrector
                .addStressToWords(generatedContentCorrector.getUnstressedCorrections(missingStress7), text7);

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
        GeneratedContentCorrector generatedContentCorrector = new GeneratedContentCorrector(wordFormRepository);

        Assertions.assertAll(
                () -> Assertions.assertEquals("В библиоте'ке я чита'ю то'лстых книг о и'стории Росси'и.",
                        generatedContentCorrector.removeSingleVowelStresses("В библиоте'ке я' чита'ю то'лстых кни'г о' и'стории Росси'и."))
        );
    }
}
