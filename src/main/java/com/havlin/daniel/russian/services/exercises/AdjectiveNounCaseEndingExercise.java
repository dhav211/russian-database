package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.dictionary.WordGender;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import com.havlin.daniel.russian.services.dictionary.WordService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class AdjectiveNounCaseEndingExercise implements Exercise{
    private final Word word;
    private final WordRetrievalService wordRetrievalService;
    private Set<String> answers;
    private String adjectiveWithNoEnding;
    private String nounInForm;

    public AdjectiveNounCaseEndingExercise(Word adjective, WordRetrievalService wordRetrievalService) {
        this.word = adjective;
        this.wordRetrievalService = wordRetrievalService;
    }

    @Override
    public void create() {
        // let's hit the word repository and choose a random noun, make sure it's in the top 10,000 nouns so its not
            // too obscure
        // now we will choose a random case, we wil do this by getting the length of the nouns word forms
        // list, then creating a random number based on that length which will choose the word form
        // now will that random word form lets choose that same word form for the adjective

        Word randomNoun = wordRetrievalService.getARandomNoun()
                .orElseThrow(() -> new FailedToCreateExerciseException("Failed to get random noun."));

        WordForm randomNounWordForm = randomNoun.getWordForms().stream().findFirst()
                .orElseThrow(() -> new FailedToCreateExerciseException("Failed to get random noun word form"));

        List<WordForm> matchingNounWordForms = randomNoun.getWordForms()
                .stream().filter((wordForm -> wordForm.getBare().equals(randomNounWordForm.getBare())))
                .toList();


        List<WordForm> adjectiveWordForms = getAdjectiveWordForms(word, matchingNounWordForms, randomNoun.getNoun().getGender());


        adjectiveWithNoEnding = word.getBare().substring(0, word.getBare().length() - 2);
        answers = adjectiveWordForms.stream().map((wordForm ->
                wordForm.getBare().substring(adjectiveWithNoEnding.length())))
                .collect(Collectors.toSet());
        nounInForm = randomNounWordForm.getBare();
    }

    @Override
    public boolean isCreated() {
        return true;
    }

    @Override
    public String toString() {
        return adjectiveWithNoEnding + "__ " + nounInForm + "      ANSWER: " + answers;
    }

    private List<WordForm> getAdjectiveWordForms(Word adjective,
                                                 List<WordForm> nounWordForms,
                                                 WordGender randomNounGender) {
        List<WordForm> adjectiveWordForms = new ArrayList<>();

        for (WordForm nounWordForm : nounWordForms) {
            String wordFormEndingSubString = getFormTypeEndingSubstring(randomNounGender, nounWordForm.getFormType());

            for (WordForm wordForm : adjective.getWordForms()) {
                int startPosition = (wordForm.getFormType().length()) - wordFormEndingSubString.length();
                String currentWordFormSubString = wordForm.getFormType().substring(startPosition);

                if (currentWordFormSubString.equals(wordFormEndingSubString)) {
                    adjectiveWordForms.add(wordForm);
                    break;
                }
            }
        }

        return adjectiveWordForms;
    }

    @NotNull
    private String getFormTypeEndingSubstring(WordGender randomNounGender, String nounFormType) {
        String[] formTypeSplit = nounFormType.split("_");
        String extractedType = formTypeSplit[formTypeSplit.length - 1];
        String extractedPlurality = formTypeSplit[formTypeSplit.length - 2];
        String adjectiveGender = "";

        if (Objects.equals(extractedPlurality, "pl")) {
            adjectiveGender = "pl";
        } else if (Objects.equals(extractedPlurality, "sg")) {
            switch (randomNounGender) {
                case MALE -> adjectiveGender = "m";
                case FEMALE -> adjectiveGender = "f";
                case NEUTER -> adjectiveGender = "n";
            }
        }

        return adjectiveGender + "_" + extractedType;
    }

    public Set<String> getAnswers() {
        return answers;
    }

    public String getAdjectiveWithNoEnding() {
        return adjectiveWithNoEnding;
    }

    public String getNounInForm() {
        return nounInForm;
    }

    private static class FormTypeIsNotANounException extends RuntimeException {
        public FormTypeIsNotANounException(String formType, String message) {
            super(formType + " isn't not a noun. The issue: " + message);
        }
    }
}
