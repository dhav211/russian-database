package com.havlin.daniel.russian.bootstrap;

import com.havlin.daniel.russian.entities.dictionary.*;
import com.havlin.daniel.russian.repositories.dictionary.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class BootstrapData implements CommandLineRunner {
    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private NounRepository nounRepository;

    @Autowired
    private VerbRepository verbRepository;

    @Autowired
    private VerbPartnerRepository verbPartnerRepository;

    @Autowired
    private WordFormRepository wordFormRepository;

    public final String DELIMITER = ";";
    public final String COMMA_DELIMITER = ",";

    @Override
    public void run(String... args) throws Exception {
        // Uncomment to recreate database
        // Uncomment to recreate database
//        createWords();
//        System.out.println("FINISHED words");
//        createTranslations();
//        System.out.println("FINISHED translations");
//        createNouns();
//        System.out.println("FINISHED nouns");
//        createVerbs();
//        System.out.println("FINISHED verbs");
//        createWordForms();
//        System.out.println("FINISHED word forms");
    }

    private void createWords() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/havlin/daniel/russian/bootstrap/openrussian_dump/words.csv"))) {
            String line;
            boolean pastInitial = false; // Ensures it won't read the first line which is just column names

            while ((line = br.readLine()) != null) {
                if (pastInitial) {
                    String[] values = line.split(DELIMITER);
                    Word word = new Word();

                    word.setId(Long.valueOf(values[0]));
                    word.setBare(values[2]);
                    word.setAccented(values[3]);

                    if (values.length > 11) {
                        switch(values[11]) {
                            case "noun":
                                word.setType(WordType.NOUN);
                                break;
                            case "verb":
                                word.setType(WordType.VERB);
                                break;
                            case "adjective":
                                word.setType(WordType.ADJECTIVE);
                                break;
                            case "adverb":
                                word.setType(WordType.ADVERB);
                                break;
                            case "expression":
                                word.setType(WordType.EXPRESSION);
                                break;
                            case "other":
                                word.setType(WordType.OTHER);
                                break;
                        }
                    }

                    if (values.length > 12) {
                        switch (values[12]) {
                            case "A1":
                                word.setWordLevel(WordLevel.A1);
                                break;
                            case "A2":
                                word.setWordLevel(WordLevel.A2);
                                break;
                            case "B1":
                                word.setWordLevel(WordLevel.B1);
                                break;
                            case "B2":
                                word.setWordLevel(WordLevel.B2);
                                break;
                            case "C1":
                                word.setWordLevel(WordLevel.C1);
                                break;
                            case "C2":
                                word.setWordLevel(WordLevel.C2);
                                break;
                        }
                    }

                    wordRepository.save(word);
                } else {
                    pastInitial = true;
                }
            }
        }
    }

    private void createTranslations() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/havlin/daniel/russian/bootstrap/openrussian_dump/translations.csv"))) {
            String line;
            boolean pastInitial = false; // Ensures it won't read the first line which is just column names

            while ((line = br.readLine()) != null) {
                if (pastInitial) {
                    String[] values = line.split(DELIMITER);

                    if (Objects.equals(values[1], "en")) {
                        Translation translation = new Translation();
                        Optional<Word> word = wordRepository.findById(Long.valueOf(values[2]));

                        translation.setTranslation(values[4]);
                        word.ifPresent(translation::setWord);
                        word.ifPresent(value -> value.getTranslations().add(translation));

                        translationRepository.save(translation);
                        word.ifPresent(value -> wordRepository.save(value));
                    }
                } else {
                    pastInitial = true;
                }
            }
        }
    }

    private void createNouns() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/havlin/daniel/russian/bootstrap/openrussian_dump/nouns.csv"))) {
            String line;
            boolean pastInitial = false; // Ensures it won't read the first line which is just column names

            while ((line = br.readLine()) != null) {
                if (pastInitial) {
                    String[] values = line.split(DELIMITER);

                    if (values.length == 0)
                        continue;

                    Noun noun = new Noun();
                    Optional<Word> word = wordRepository.findById(Long.valueOf(values[0]));

                    word.ifPresent(noun::setWord);
                    word.ifPresent(value -> value.setNoun(noun));

                    if (values.length > 1)
                    {
                        switch (values[1]) {
                            case "f":
                                noun.setGender(WordGender.FEMALE);
                                break;
                            case "m":
                                noun.setGender(WordGender.MALE);
                                break;
                            case "n":
                                noun.setGender(WordGender.NEUTER);
                                break;
                        }
                    }

                    if (values.length > 2)
                        noun.setPartner(values[2]);
                    if (values.length > 3)
                        noun.setAnimate(!Objects.equals(values[3], "0"));
                    if (values.length > 4)
                        noun.setIndeclinable(!Objects.equals(values[4], "0"));
                    if (values.length > 5)
                        noun.setSingularOnly(!Objects.equals(values[5], "0"));
                    if (values.length > 6)
                        noun.setPluralOnly(!Objects.equals(values[6], "0"));

                    nounRepository.save(noun);
                    word.ifPresent(value -> wordRepository.save(value));
                } else {
                    pastInitial = true;
                }
            }
        }
    }

    private void createVerbs() throws Exception{
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/havlin/daniel/russian/bootstrap/openrussian_dump/verbs.csv"))) {
            String line;
            boolean pastInitial = false; // Ensures it won't read the first line which is just column names

            while ((line = br.readLine()) != null) {
                if (pastInitial) {
                    String[] values = line.split(COMMA_DELIMITER);

                    if (values.length == 0)
                        continue;

                    Verb verb = new Verb();
                    Optional<Word> word = wordRepository.findById(Long.valueOf(values[0]));

                    word.ifPresent(value -> value.setVerb(verb));
                    word.ifPresent(verb::setWord);

                    if (values.length > 1) {
                        switch (values[1]) {
                            case "both":
                                verb.setAspect(VerbAspect.BOTH);
                                break;
                            case "perfective":
                                verb.setAspect(VerbAspect.PERFECTIVE);
                                break;
                            case "imperfective":
                                verb.setAspect(VerbAspect.IMPERFECTIVE);
                                break;
                        }
                    }

                    verbRepository.save(verb);

                    if (values.length > 2) {
                        String partnersText = values[2];
                        partnersText = partnersText.replace("\"", "");

                        String[] partners = partnersText.split(";");

                        for (String partner : partners) {
                            VerbPartner verbPartner = new VerbPartner();

                            verbPartner.setText(partner);
                            verbPartner.setVerb(verb);
                            verb.getPartners().add(verbPartner);

                            verbPartnerRepository.save(verbPartner);
                        }
                    }

                    verbRepository.save(verb);
                    word.ifPresent(value -> wordRepository.save(value));
                } else {
                    pastInitial = true;
                }
            }
        }
    }

    private void createWordForms() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/havlin/daniel/russian/bootstrap/openrussian_dump/words_forms.csv"))) {
            String line;
            boolean pastInitial = false; // Ensures it won't read the first line which is just column names

            while ((line = br.readLine()) != null) {
                if (pastInitial) {
                    String[] values = line.split(DELIMITER);

                    if (values.length == 0) {
                        continue;
                    }

                    WordForm wordForm = new WordForm();
                    Optional<Word> word = wordRepository.findById(Long.valueOf(values[1]));

                    word.ifPresent(wordForm::setWord);
                    word.ifPresent(value -> value.getWordForms().add(wordForm));

                    // TODO debug this before building the database to ensure the ( ) are being removed
                    if (values.length > 2) {
                        // Remove the parenthesis from the word.
                        if (wordForm.getAccented().contains("(")) {
                            StringBuilder stringBuilder = new StringBuilder(values[2]);
                            stringBuilder.deleteCharAt(0);
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            values[2] = stringBuilder.toString();
                        }
                        wordForm.setFormType(values[2]);
                    }

                    if (values.length > 4) {
                        if (wordForm.getAccented().contains("(")) {
                            StringBuilder stringBuilder = new StringBuilder(values[4]);
                            stringBuilder.deleteCharAt(0);
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            values[4] = stringBuilder.toString();
                        }
                        wordForm.setAccented(values[4]);
                    }

                    if (values.length > 5) {
                        wordForm.setBare(values[5]);
                    }

                    wordFormRepository.save(wordForm);
                    word.ifPresent(value -> wordRepository.save(value));
                } else {
                    pastInitial = true;
                }
            }
        }
    }
}
