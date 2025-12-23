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

class AdjectiveNounCaseEndingExercise implements Exercise{
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
        // We will first need a random noun to pair with our adjective
        Word randomNoun = wordRetrievalService.getARandomNoun()
                .orElseThrow(() -> new FailedToCreateExerciseException("Failed to get random noun."));

        // Since the particular set we are using never stores information in the same order we can just grab the first
        // and assume that it's random
        WordForm randomNounWordForm = randomNoun.getWordForms().stream().findFirst()
                .orElseThrow(() -> new FailedToCreateExerciseException("Failed to get random noun word form"));

        // It's highly probable that there will be multiple word forms that have the same ending
        // we will just grab all of them so we can get the answers for our adjective ending
        List<WordForm> matchingNounWordForms = randomNoun.getWordForms()
                .stream().filter((wordForm -> wordForm.getBare().equals(randomNounWordForm.getBare())))
                .toList();

        // Here we can just get all the possible matching word forms for the adjectives, so the noun and adjective forms can agree
        List<WordForm> adjectiveWordForms = getAdjectiveWordForms(word, matchingNounWordForms, randomNoun.getNoun().getGender());

        // At this point we have all the required data to form the exercise
        try {
            adjectiveWithNoEnding = word.getBare().substring(0, word.getBare().length() - 2);
            answers = adjectiveWordForms.stream().map((wordForm ->
                            wordForm.getBare().substring(adjectiveWithNoEnding.length())))
                    .collect(Collectors.toSet());
            nounInForm = randomNounWordForm.getBare();
        } catch (StringIndexOutOfBoundsException e) {
            throw new FailedToCreateExerciseException("Went out of bounds trying to find the substring in the word form text");
        }
    }

    @Override
    public String toString() {
        return adjectiveWithNoEnding + "__ " + nounInForm + "      ANSWER: " + answers;
    }

    /**
     * Grab all the possible word forms the adjective could be when compared to the noun word forms.
     * @param adjective Must be an adjective or method will return an empty list.
     * @param nounWordForms All the noun's word forms, the ending of the word will be the same but is still in a
     *                      different form.
     * @param randomNounGender The gender of the noun, will be needed if the form isn't in plural
     * @return A list of the adjective's word forms based upon the given random noun's word forms
     */
    private List<WordForm> getAdjectiveWordForms(Word adjective,
                                                 List<WordForm> nounWordForms,
                                                 WordGender randomNounGender) {
        List<WordForm> adjectiveWordForms = new ArrayList<>();

        // We will go through each of the random noun's word form and create a string that will be compared to the
        // ending of the adjectives word form type
        for (WordForm nounWordForm : nounWordForms) {
            String wordFormEndingSubString = getFormTypeEndingSubstring(randomNounGender, nounWordForm.getFormType());

            // Now that we have the constructed word form ending which may look something like "m_acc" or "f_dat"\
            // We can loop through each of the adjectives word form and check to see if this ending lines up
            // if it does line up then we know that's the word form we are looking for, so lets add it to the list
            // and then try the next word form of the noun
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
