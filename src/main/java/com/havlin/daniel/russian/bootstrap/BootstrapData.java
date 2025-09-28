package com.havlin.daniel.russian.bootstrap;

import com.havlin.daniel.russian.entities.dictionary.*;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.repositories.dictionary.*;
import com.havlin.daniel.russian.repositories.generated_content.DefinitionRepository;
import com.havlin.daniel.russian.services.dictionary.DefinitionService;
import com.havlin.daniel.russian.services.dictionary.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
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

    @Autowired
    private DefinitionRepository definitionRepository;

    @Autowired
    private DefinitionService definitionService;

    @Autowired
    private SentenceService sentenceService;

    public final String DELIMITER = ";";
    public final String COMMA_DELIMITER = ",";

    @Override
    public void run(String... args) throws Exception {
        //if (wordRepository.count() == 0) {
            //System.out.println("Creating database from csv files");
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
        //}

        //String word = StressedWordConverter.addStressMarks("домашнее'");
        //String word = StressedWordConverter.removeStressMarks("людьми́");

        //sentenceService.createSentencesForWord(wordRepository.findById(170L).get());

    }

    private void createMockDefinition() {
        // This is the prompt for short definition
        //Write a short one sentence definition for the word собака in B1 level Russian. Put a ' after the letter of the word that needs to be stressed. Do not use the word собака and do not include a —.

        // This is the prompt for long definition
        //Write a multiple sentence definition for the word собака using B1 level Russian. Put a ' after the letter of the word that needs to be stressed. Do not use the word собака and do not include a —

        String dogShortDefinition = "Э'то дома'шнее живо'тное, кото'рое о'чень лю'бит люде'й и мо'жет охраня'ть дом.";
        String dogLongDefinition = "Э'то дома'шнее живо'тное, кото'рое живё'т с людьми'. Оно' о'чень ве'рное и у'мное. Мо'жет охраня'ть дом и игра'ть с детьми'. Лю'бит гуля'ть, бе'гать и есть специа'льную е'ду. Быва'ет ра'зных разме'ров и цвето'в. Мно'гие семьи' держа'т тако'го пито'мца дома'.";
        //Word cobaka = wordRepository.findById(465L).get();
        Definition definition = new Definition();
        definition.setShortDefinition(dogShortDefinition);
        definition.setLongDefinition(dogLongDefinition);

    }

    private void createMockSentences() {
        String sentences = "Соба'ка лю'бит игра'ть в па'рке.\n" +
                "The dog loves to play in the park.\n" +
                "У меня' есть кра'сивая соба'ка.\n" +
                "I have a beautiful dog.\n" +
                "Де'ти дают еду' соба'ке.\n" +
                "The children give food to the dog.\n" +
                "Мы ви'дим соба'ку на у'лице.\n" +
                "We see a dog on the street.\n" +
                "Ребёнок игра'ет с соба'кой.\n" +
                "The child plays with the dog.\n" +
                "Мы говори'м о соба'ке.\n" +
                "We are talking about the dog.\n" +
                "Соба'ки бе'гают по двору'.\n" +
                "Dogs run around the yard.\n" +
                "У мои'х сосе'дей нет соба'к.\n" +
                "My neighbors don't have dogs.\n" +
                "Ветерина'р да'рит лека'рства соба'кам.\n" +
                "The veterinarian gives medicine to the dogs.\n" +
                "Мы лю'бим э'тих соба'к.\n" +
                "We love these dogs.\n" +
                "Де'ти гуля'ют с соба'ками.\n" +
                "The children walk with the dogs.\n" +
                "В кни'ге расска'з о соба'ках.\n" +
                "The book is a story about dogs.";
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

                    if (values.length > 2) {
                        wordForm.setFormType(values[2]);
                    }

                    if (values.length > 4) {
                        // Remove the parenthesis from the word.
                        if (values[4].contains("(")) {
                            StringBuilder stringBuilder = new StringBuilder(values[4]);
                            stringBuilder.deleteCharAt(0);
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            values[4] = stringBuilder.toString();
                        }
                        wordForm.setAccented(values[4]);
                    }

                    if (values.length > 5) {
                        // Remove the parenthesis from the word.
                        if (values[5].contains("(")) {
                            StringBuilder stringBuilder = new StringBuilder(values[5]);
                            stringBuilder.deleteCharAt(0);
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            values[5] = stringBuilder.toString();
                        }
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
