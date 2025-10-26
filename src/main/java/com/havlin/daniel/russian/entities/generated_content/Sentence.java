package com.havlin.daniel.russian.entities.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
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

    @Enumerated(EnumType.STRING)
    private ReadingLevel readingLevel;

    @Enumerated(EnumType.STRING)
    private GeneratedSentenceGrammarForm grammarForm;

    @Enumerated(EnumType.STRING)
    private GeneratedContentStatus status;

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
        grammarForm = grammarForm.toUpperCase();

        switch (grammarForm) {
            case "NOMINATIVE":
                this.setGrammarForm(GeneratedSentenceGrammarForm.NOMINATIVE);
                break;
            case "GENITIVE":
                this.setGrammarForm(GeneratedSentenceGrammarForm.GENITIVE);
                break;
            case "ACCUSATIVE":
                this.setGrammarForm(GeneratedSentenceGrammarForm.ACCUSATIVE);
                break;
            case "PREPOSITIONAL":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PREPOSITIONAL);
                break;
            case "INSTRUMENTAL":
                this.setGrammarForm(GeneratedSentenceGrammarForm.INSTRUMENTAL);
                break;
            case "DATIVE":
                this.setGrammarForm(GeneratedSentenceGrammarForm.DATIVE);
                break;
            case "FIRST PERSON SINGULAR":
                this.setGrammarForm(GeneratedSentenceGrammarForm.FIRST_PERSON_SINGULAR);
                break;
            case "SECOND PERSON SINGULAR":
                this.setGrammarForm(GeneratedSentenceGrammarForm.SECOND_PERSON_SINGULAR);
                break;
            case "THIRD PERSON SINGULAR":
                this.setGrammarForm(GeneratedSentenceGrammarForm.THIRD_PERSON_SINGULAR);
                break;
            case "FIRST PERSON PLURAL":
                this.setGrammarForm(GeneratedSentenceGrammarForm.FIRST_PERSON_PLURAL);
                break;
            case "SECOND PERSON PLURAL":
                this.setGrammarForm(GeneratedSentenceGrammarForm.SECOND_PERSON_PLURAL);
                break;
            case "THIRD PERSON PLURAL":
                this.setGrammarForm(GeneratedSentenceGrammarForm.THIRD_PERSON_PLURAL);
                break;
            case "PAST", "NEUTER PAST":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PAST);
                break;
            case "IMPERATIVE SINGULAR":
                this.setGrammarForm(GeneratedSentenceGrammarForm.IMPERATIVE_SINGULAR);
                break;
            case "IMPERATIVE PLURAL":
                this.setGrammarForm(GeneratedSentenceGrammarForm.IMPERATIVE_PLURAL);
                break;
            case "GERUND PAST":
                this.setGrammarForm(GeneratedSentenceGrammarForm.GERUND_PAST);
                break;
            case "GERUND PRESENT":
                this.setGrammarForm(GeneratedSentenceGrammarForm.GERUND_PRESENT);
                break;
            case "PARTICIPLE ACTIVE PRESENT":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_ACTIVE_PRESENT);
                break;
            case "PARTICIPLE ACTIVE PAST":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_ACTIVE_PAST);
                break;
            case "PARTICIPLE PASSIVE PRESENT":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_PASSIVE_PRESENT);
                break;
            case "PARTICIPLE PASSIVE PAST":
                this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_PASSIVE_PAST);
                break;
            case "SUPERLATIVE":
                this.setGrammarForm(GeneratedSentenceGrammarForm.SUPERLATIVE);
                break;
            case "COMPARATIVE":
                this.setGrammarForm(GeneratedSentenceGrammarForm.COMPARATIVE);
                break;
            case "SHORT":
                this.setGrammarForm(GeneratedSentenceGrammarForm.SHORT);
                break;
            default:
                this.setGrammarForm(GeneratedSentenceGrammarForm.ERROR);
                break;
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

    public GeneratedContentStatus getStatus() {
        return status;
    }

    public void setStatus(GeneratedContentStatus status) {
        this.status = status;
    }
}
