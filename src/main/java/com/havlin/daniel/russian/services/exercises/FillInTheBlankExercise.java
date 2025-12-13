package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.repositories.generated_content.SentenceRepository;

import java.util.*;
import java.util.stream.Collectors;

public class FillInTheBlankExercise implements Exercise {
    private final Word word;

    public FillInTheBlankExercise(Word word) {
        this.word = word;
    }

    /**
     * This is in the method
     */
    @Override
    public void create() {
        if (word.getSentences() == null)
            throw new FailedToCreateExerciseException("The sentences for the word " + word + " have not been set");

        else if (word.getWordForms() == null)
            throw new FailedToCreateExerciseException("The word forms for the word " + word + " have not been set");

        else if (word.getSentences().isEmpty())
            throw new FailedToCreateExerciseException("The word " + word.getBare() + " does not have any sentences");


        // grab a sentence and figure out which word form is used in that sentence
        // now we will need to get the substring position of that word form
        // we will probably have to brute for locate it
        // loop through each character, then try to match the word
        // once we hit the end of the word check to see if the next letter is a space or punctuation mark.
        // once we got the start and end position figured out we can extract the word from the sentence.
        // so if the start is 12 and the end is 17 we get the first substring sentence.substring(0, 12)
        // we are ultimately trying to add these substrings to an array for example
        // "На'м всегда' бы'ло хорошо' вме'сте, несмо'тря на все' тру'дности." the word is хорошо'
            // "На'м всегда' бы'ло "
            // "---"
            // " вме'сте, несмо'тря на все' тру'дности."
        // There are some edge cases like literally dealing with the edges.
        // if the start of the substring is 0 then we will just push that "---" to the front of the array

        Sentence sentence = word.getSentences().stream().findFirst()
                .orElseThrow(() -> new FailedToCreateExerciseException("Failed to find first sentence in the word " + word.getBare()));

        String usedWordForm = getUsedWordForm(word.getAccented(), word.getWordForms(), sentence.getText())
                .orElseThrow(() -> new FailedToCreateExerciseException("Failed to find required word form in sentence"));

        WordFormSubstringPosition substringPosition = getWordFormSubstringPosition(usedWordForm, sentence.getText());

        System.out.println("");
    }

    private Optional<String> getUsedWordForm(String baseWord, Set<WordForm> wordForms, String sentenceText) {
        // Uppercase and remove all punctuation to make it easier to compare
        List<String>  splitSentence = Arrays.stream(sentenceText.replaceAll("[!”#$%&()*+,./:\"]", "").
                toUpperCase().split(" "))
                .toList();

        // let's reduce the number of words we will compare to by just adding words that contain the first half of the base word
        Set<String> possibleWords = splitSentence.stream().filter((word) ->
                        word.contains(baseWord.substring(0, baseWord.length() / 2)))
                .collect(Collectors.toSet());

        Optional<String> foundWordForm = Optional.empty();
        boolean hasFoundWordForm = false;

        // We will brute force compare all the word forms with the possible word choices
        // once it has a match it will fill the optional and exit both loops
        for (WordForm wordForm : wordForms) {
            for (String possibleWord : possibleWords) {
                if (wordForm.getAccented().toUpperCase().equals(possibleWord)) {
                    foundWordForm = Optional.of(wordForm.getAccented());
                    hasFoundWordForm = true;
                    break;
                }
            }

            if (hasFoundWordForm) {
                break;
            }
        }

        return foundWordForm;
    }

    private WordFormSubstringPosition getWordFormSubstringPosition(String usedWordForm, String sentence) {
        int start = 0;
        int end = 0;

        boolean hasFoundSubstring = false;

        for (int i = 0; i < sentence.length(); i++) {
            start = i;
            if (sentence.charAt(i) == usedWordForm.charAt(0)) {
                for (int j = 1; j <= usedWordForm.length(); j++) {
                    if (j < usedWordForm.length() && sentence.charAt(i + j) != usedWordForm.charAt(i)) {
                        break;
                    } else if (j == usedWordForm.length() && "!”#$%&()*+,./:\"".indexOf(sentence.charAt(i + j)) > -1) {
                        hasFoundSubstring = true;
                        end = i + j - 1;
                    }
                }
            }

            if (hasFoundSubstring)
                break;
        }

        if (!hasFoundSubstring) {
            start = 0;
            end = 0;
        }

        return new WordFormSubstringPosition(start, end);
    }

    public int getWordPosition(String sentence, String wordForm) {
        // If this remains as -1 it will mean an error occurred
        int wordPosition = -1;

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

        return wordPosition;
    }

    private record WordFormSubstringPosition(int start, int end) {}
}
