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
        grammarForm = grammarForm.toUpperCase();

        if (grammarForm.contains("NOMINATIVE"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.NOMINATIVE);
        else if (grammarForm.contains("GENITIVE"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.GENITIVE);
        else if (grammarForm.contains( "ACCUSATIVE"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.ACCUSATIVE);
        else if (grammarForm.contains( "PREPOSITIONAL"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.PREPOSITIONAL);
        else if (grammarForm.contains( "INSTRUMENTAL"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.INSTRUMENTAL);
        else if (grammarForm.contains( "DATIVE"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.DATIVE);
        else if (grammarForm.contains("FIRST PERSON SINGULAR"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.FIRST_PERSON_SINGULAR);
        else if (grammarForm.contains("SECOND PERSON SINGULAR"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.SECOND_PERSON_SINGULAR);
        else if (grammarForm.contains("THIRD PERSON SINGULAR"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.THIRD_PERSON_SINGULAR);
        else if (grammarForm.contains( "FIRST PERSON PLURAL"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.FIRST_PERSON_PLURAL);
        else if (grammarForm.contains( "SECOND PERSON PLURAL"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.SECOND_PERSON_PLURAL);
        else if (grammarForm.contains( "THIRD PERSON PLURAL"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.THIRD_PERSON_PLURAL);
        else if (grammarForm.contains("PAST"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.PAST);
        else if (grammarForm.contains("IMPERATIVE SINGULAR"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.IMPERATIVE_SINGULAR);
        else if (grammarForm.contains("IMPERATIVE PLURAL"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.IMPERATIVE_PLURAL);
        else if (grammarForm.contains( "GERUND PAST"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.GERUND_PAST);
        else if (grammarForm.contains( "GERUND PRESENT"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.GERUND_PRESENT);
        else if (grammarForm.contains( "PARTICIPLE ACTIVE PRESENT"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_ACTIVE_PRESENT);
        else if (grammarForm.contains( "PARTICIPLE ACTIVE PAST"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_ACTIVE_PAST);
        else if (grammarForm.contains( "PARTICIPLE PASSIVE PRESENT"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_PASSIVE_PRESENT);
        else if (grammarForm.contains( "PARTICIPLE PASSIVE PAST"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.PARTICIPLE_PASSIVE_PAST);
        else if (grammarForm.contains( "SUPERLATIVE"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.SUPERLATIVE);
        else if (grammarForm.contains( "COMPARATIVE"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.COMPARATIVE);
        else if (grammarForm.contains( "SHORT"))
            this.setGrammarForm(GeneratedSentenceGrammarForm.SHORT);
        else
            this.setGrammarForm(GeneratedSentenceGrammarForm.ERROR);

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
