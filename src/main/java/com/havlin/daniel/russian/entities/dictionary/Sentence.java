package com.havlin.daniel.russian.entities.dictionary;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    private String text;

    private String englishTranslation;

    private ReadingLevel readingLevel;

    private GeneratedSentenceGrammarForm grammarForm;

    private int wordPosition;

    public Long getId() {
        return id;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    public void setEnglishTranslation(String englishTranslation) {
        this.englishTranslation = englishTranslation;
    }

    public ReadingLevel getReadingLevel() {
        return readingLevel;
    }

    public void setReadingLevel(ReadingLevel readingLevel) {
        this.readingLevel = readingLevel;
    }

    public GeneratedSentenceGrammarForm getGrammarForm() {
        return grammarForm;
    }

    public void setGrammarForm(GeneratedSentenceGrammarForm grammarForm) {
        this.grammarForm = grammarForm;
    }

    public void setGrammarFormFromString(String grammarForm) {
        // grammarForm string is given from the generated prompt which is held in the SentenceSet POJO
        switch (grammarForm) {
            case "Nominative":
                this.setGrammarForm(GeneratedSentenceGrammarForm.NOMINATIVE);
                break;
            case "Genitive":
                this.setGrammarForm(GeneratedSentenceGrammarForm.GENITIVE);
                break;
            case "Accusative":
                this.setGrammarForm(GeneratedSentenceGrammarForm.ACCUSATIVE);
                break;
            case "Prepositional":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PREPOSITIONAL);
                break;
            case "Instrumental":
                this.setGrammarForm(GeneratedSentenceGrammarForm.INSTRUMENTAL);
                break;
            case "Dative":
                this.setGrammarForm(GeneratedSentenceGrammarForm.DATIVE);
                break;
            case "First Person Singular":
                this.setGrammarForm(GeneratedSentenceGrammarForm.FIRST_PERSON_SINGULAR);
                break;
            case "Second Person Singular":
                this.setGrammarForm(GeneratedSentenceGrammarForm.SECOND_PERSON_SINGULAR);
                break;
            case "Third Person Singular":
                this.setGrammarForm(GeneratedSentenceGrammarForm.THIRD_PERSON_SINGULAR);
                break;
            case "First Person Plural":
                this.setGrammarForm(GeneratedSentenceGrammarForm.FIRST_PERSON_PLURAL);
                break;
            case "Second Person Plural":
                this.setGrammarForm(GeneratedSentenceGrammarForm.SECOND_PERSON_PLURAL);
                break;
            case "Third Person Plural":
                this.setGrammarForm(GeneratedSentenceGrammarForm.THIRD_PERSON_PLURAL);
                break;
            case "Past":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PAST);
                break;
            case "Imperative Singular":
                this.setGrammarForm(GeneratedSentenceGrammarForm.IMPERATIVE_SINGULAR);
                break;
            case "Imperative Plural":
                this.setGrammarForm(GeneratedSentenceGrammarForm.IMPERATIVE_PLURAL);
                break;
            case "Gerund Past":
                this.setGrammarForm(GeneratedSentenceGrammarForm.GERUND_PAST);
                break;
            case "Gerund Present":
                this.setGrammarForm(GeneratedSentenceGrammarForm.GERUND_PRESENT);
                break;
            case "Participle Active Present":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_ACTIVE_PRESENT);
                break;
            case "Participle Active Past":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_ACTIVE_PAST);
                break;
            case "Participle Passive Present":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_PASSIVE_PRESENT);
                break;
            case "Participle Passive Past":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_PASSIVE_PAST);
                break;
            case "Superlative":
                this.setGrammarForm(GeneratedSentenceGrammarForm.SUPERLATIVE);
                break;
            case "Comparative":
                this.setGrammarForm(GeneratedSentenceGrammarForm.COMPARATIVE);
                break;
            case "Short form", "Short":
                this.setGrammarForm(GeneratedSentenceGrammarForm.SHORT);
                break;
            default:
                this.setGrammarForm(GeneratedSentenceGrammarForm.ERROR);
        }
    }

    public int getWordPosition() {
        return wordPosition;
    }

    public void setWordPosition(String sentence, String wordForm) {
        // If this remains as -1 it will mean an error occurred and sentence will be rejected
        wordPosition = -1;
        // Remove the punctuation from the sentence. Not all of it as some will still be needed for word detection
        sentence = sentence.replaceAll("[.,!?]", "").toUpperCase();
        // Break down sentence into individual words and loop through and find the match with word form
        String[] sentenceSplit = sentence.split(" ");
        for (int i = 0; i < sentenceSplit.length; i++) {
            if (Objects.equals(sentenceSplit[i], wordForm)) {
                wordPosition = i;
                break;
            }
        }
    }
}
