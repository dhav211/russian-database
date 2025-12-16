package com.havlin.daniel.russian.services.exercises;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.dictionary.WordForm;
import com.havlin.daniel.russian.entities.dictionary.WordType;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.entities.users.LearnedWord;
import com.havlin.daniel.russian.repositories.generated_content.SentenceRepository;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import com.havlin.daniel.russian.services.users.LearnedWordService;

import java.util.*;
import java.util.stream.Collectors;

public class FillInTheBlankExercise implements Exercise {
    private final Word word;
    private final Random random;
    private Set<LearnedWord> learnedWords;
    private final WordRetrievalService wordRetrievalService;
    private final List<String> sentenceWithBlank = new ArrayList<>();
    private final Map<String, Boolean> answerKey = new HashMap<>();

    public FillInTheBlankExercise(Word word, Random random, Set<LearnedWord> learnedWords, WordRetrievalService wordRetrievalService) {
        this.word = word;
        this.random = random;
        this.learnedWords = learnedWords;
        this.wordRetrievalService = wordRetrievalService;
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

        Sentence sentence = word.getSentences().stream().skip(random.nextInt(word.getSentences().size())).findFirst()
                .orElseThrow(() -> new FailedToCreateExerciseException("Failed to find random sentence in the word " + word.getBare()));

        Set<String> usedWordForms = getUsedWordForms(word.getAccented(), word.getWordForms(), sentence.getText());

        if (usedWordForms.isEmpty())
            throw new FailedToCreateExerciseException("Failed to find word forms in sentence");

        WordFormSubstringPosition substringPosition = getWordFormSubstringPosition(usedWordForms, sentence.getText());

        try {
            if (substringPosition.start() == 0) {
                sentenceWithBlank.add("---");
                sentenceWithBlank.add(sentence.getText().substring(substringPosition.end() + 1));
            } else {
                sentenceWithBlank.add(sentence.getText().substring(0, substringPosition.start()));
                sentenceWithBlank.add("---");
                sentenceWithBlank.add(sentence.getText().substring(substringPosition.end() + 1));
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new FailedToCreateExerciseException("Failed to get substring from sentence: " + e.getMessage());
        }

        try {
            String usedWordForm = usedWordForms.stream().findFirst()
                    .orElseThrow(() -> new FailedToCreateExerciseException("Failed to get first word from usedWordForms set"));
            fillAnswerKey(usedWordForm);
        } catch (NullPointerException e) {
            throw new FailedToCreateExerciseException("Failed to fill answer key");
        }
    }

    private Set<String> getUsedWordForms(String baseWord, Set<WordForm> wordForms, String sentenceText) {
        Set<String> matchingWordForms = new HashSet<>();

        // Uppercase and remove all punctuation to make it easier to compare
        String noAccentBaseWord = baseWord.replaceAll("['!”#$%&()*+,./:\"]", "").toUpperCase();
        List<String>  splitSentence = Arrays.stream(sentenceText.replaceAll("['!”#$%&()*+,./:\"]", "").
                toUpperCase().split(" "))
                .toList();

        // let's reduce the number of words we will compare to by just adding words that contain the first half of the base word
        Set<String> possibleWords = splitSentence.stream().filter((word) ->
                        word.contains(noAccentBaseWord.substring(0, noAccentBaseWord.length() / 2)))
                .collect(Collectors.toSet());

        // We will brute force compare all the word forms with the possible word choices
        // once it has a match it will fill the optional and exit both loops
        for (WordForm wordForm : wordForms) {
            for (String possibleWord : possibleWords) {
                if (wordForm.getBare().toUpperCase().equals(possibleWord)) {
                    matchingWordForms.add(wordForm.getAccented());
                }
            }
        }

        return matchingWordForms;
    }

    private void fillAnswerKey(String usedWordForm) {
        // Choose up to 20 random learned words, this may be less than 20 if there are less than 20 words in the dictionary
        int collectionCount = 0;
        int collectionSize = Math.min(learnedWords.size() - 1, 20);
        List<Long> idsToGet = new ArrayList<>();
        for (LearnedWord learnedWord : learnedWords) {
            idsToGet.add(learnedWord.getWordId());
            collectionCount++;

            if (collectionCount == collectionSize) {
                break;
            }
        }

        // The random IDs we grabbed from the learned words can be used to fetch the words
        // we will also only accept words that are nouns and not actual answer
        List<Word> randomNouns = new ArrayList<>(wordRetrievalService.getWordsByIdsWithWordForms(idsToGet)
                .stream().filter((randomWord) ->
                        randomWord.getType() == WordType.NOUN && !word.getAccented().equals(randomWord.getAccented()))
                .toList());

        // If we don't have enough random nouns from our learned words lets actually choose some random nouns from the
        // word repository
        if (randomNouns.size() < 2) {
            for (int i = 0; i < 2 - randomNouns.size(); i++) {
                Word randomNoun = wordRetrievalService.getARandomNoun().orElseThrow(NullPointerException::new);
                randomNouns.add(randomNoun);
            }
        }

        // Choose 2 random nouns randomly as possible answers, if there are only 2 in the list then those are our answers
        List<Word> possibleAnswers = new ArrayList<>();
        if (randomNouns.size() == 2) {
            possibleAnswers.addAll(randomNouns);
        } else {
            int attempts = 0;
            boolean areAnswersDifferent = false;
            while (!areAnswersDifferent && attempts < 2) {
                // this generates a list of random ints, then uses them to choose from out random noun list
                possibleAnswers.clear();
                random.ints(2, 0, randomNouns.size())
                        .forEach((randomChoice) -> possibleAnswers.add(randomNouns.get(randomChoice)));

                // the main condition to exit the while loop
                areAnswersDifferent = possibleAnswers.size() != 2
                        || !possibleAnswers.get(0).getAccented().equals(possibleAnswers.get(1).getAccented());

                // if there are more than 3 attempts we will just assume it cant be done and move forward
                attempts++;
            }
        }

        // We are looking for the form type string of the answer, for example ru_noun_sg_gen
        // This is being done so we can match our possible answers together in the same case
        String usedFormType = word.getWordForms().stream()
                .filter((wf) -> wf.getAccented().equals(usedWordForm))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Couldn't find a matching word form"))
                .getFormType();


        // The possible answers are in the database word format, we really just need a string of the matching word form
        // so here we compare the usedFormType was got above to get the proper word form for our possible answers
        for (Word possibleAnswer : possibleAnswers) {
           String matchingWordForm = possibleAnswer.getWordForms().stream()
                   .filter((wf) -> wf.getFormType().equals(usedFormType))
                   .findFirst()
                   .orElseThrow(() -> new NullPointerException("Couldn't find a matching word form"))
                   .getAccented();

           answerKey.put(matchingWordForm, false);
        }

        answerKey.put(usedWordForm, true);
    }

    private WordFormSubstringPosition getWordFormSubstringPosition(Set<String> usedWordForms, String sentence) {
        int start = 0;
        int end = 0;

        boolean hasFoundSubstring = false;

        for (String wordForm : usedWordForms) {
            for (int i = 0; i < sentence.length(); i++) {
                start = i;
                if (sentence.charAt(i) == wordForm.charAt(0)) {
                    for (int j = 1; j <= wordForm.length(); j++) {
                        if (j < wordForm.length() && sentence.charAt(i + j) != wordForm.charAt(j)) {
                            break;
                        } else if (j == wordForm.length() && "!”#$%&()*+,./:\" ".indexOf(sentence.charAt(i + j)) > -1) {
                            hasFoundSubstring = true;
                            end = i + j - 1;
                        }
                    }
                }

                if (hasFoundSubstring) // exit inner loop, looping through each character of the sentence
                    break;
            }
            if (hasFoundSubstring) // exit outer loop, looping through each possible variation of word
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

    public List<String> getSentenceWithBlank() {
        return sentenceWithBlank;
    }

    public Map<String, Boolean> getAnswerKey() {
        return answerKey;
    }
}
