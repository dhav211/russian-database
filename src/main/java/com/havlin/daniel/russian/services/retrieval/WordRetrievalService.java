package com.havlin.daniel.russian.services.retrieval;

import com.havlin.daniel.russian.entities.dictionary.*;
import com.havlin.daniel.russian.entities.retrieval.*;
import com.havlin.daniel.russian.repositories.dictionary.WordFormRepository;
import com.havlin.daniel.russian.repositories.dictionary.WordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordRetrievalService {
    private static final Logger log = LoggerFactory.getLogger(WordRetrievalService.class);
    private final WordRepository wordRepository;
    private final WordFormRepository wordFormRepository;
    private final Random random;

    public WordRetrievalService(WordRepository wordRepository, WordFormRepository wordFormRepository) {
        this.wordRepository = wordRepository;
        this.wordFormRepository = wordFormRepository;
        this.random = new Random();
    }

    public WordType getWordTypeFromWordId(Long id) {
        try {
            WordType wordType = wordRepository.findWordTypeById(id);

            return Objects.requireNonNullElse(wordType, WordType.ERROR);
        } catch (Exception e) {
            return WordType.ERROR;
        }
    }

    public List<String> findAllWordFormsWithUniqueWordByBare(String bare) {
        Map<Long, String> wordFormsWithUniqueWords = new HashMap<>();
        for (WordForm wf : wordFormRepository.findAllByBare(bare)) {
            if (!wordFormsWithUniqueWords.containsKey(wf.getWord().getId())) {
                wordFormsWithUniqueWords.put(wf.getWord().getId(), wf.getAccented());
            }
        }

        if (wordFormsWithUniqueWords.isEmpty()) {
            List<Word> words = wordRepository.findAllByBare(bare);
            if (words.size() == 1) {
                wordFormsWithUniqueWords.put(words.getFirst().getId(), words.getFirst().getAccented());
            }
        }
        return wordFormsWithUniqueWords.values().stream().toList();
    }

    public boolean doesWordFormExistByAccented(String accented) {
        return wordFormRepository.existsByAccented(accented) || wordRepository.existsByAccented(accented);
    }

    public List<String> findAllAccentedWordFormsByBare(String bare) {
        return wordFormRepository.findAccentedByBare(bare);
    }

    public Set<Word> findAllWordsByAccentedForSentenceCreation(String accented) {
        Set<Word> foundWords = new HashSet<>(wordFormRepository.findWordByAccentedForSentenceCreation(accented));
        if (foundWords.isEmpty())
            foundWords.addAll(wordRepository.findWordsByAccentedForContentCreation(accented));

        return foundWords;
    }

    /**
     * Find all the details the user will see when they click on a word while reading a book in the frontend.
     * @param bareText The word without any stress marks that will be searched
     * @return There may be more than one match found since this is search with no stress.
     */
    public Set<ClickedWordDTO> getClickedWord(String bareText) {
        Set<ClickedWordDTO> clickedWordDTOs = new HashSet<>();
        Set<Word> foundWords = wordFormRepository.findAllMatchWordsByBareWordForm(bareText);

        for (Word word : foundWords) {
            clickedWordDTOs.add(new ClickedWordDTO(word));
        }

        return clickedWordDTOs;
    }

    public Set<Word> getWordsFromBareText(String bareText) {
        List<WordForm> matchingWordForms = wordFormRepository.findAllByBare(bareText);
        return sortWordsFromWordFormsById(matchingWordForms);
    }

    public Set<Word> getWordsFromAccentedText(String accentedText) {
        List<WordForm> matchingWordsForms = wordFormRepository.findAllByAccented(accentedText);
        return sortWordsFromWordFormsById(matchingWordsForms);
    }

    public Optional<Word> getWordByAccentedTextForSentenceCreation(String accentedText) {
        return Optional.of(wordRepository.findWordsByAccentedForContentCreation(accentedText).getFirst());
    }

    public Optional<Word> getWordByIdForSentenceCreation(Long id) {
        return wordRepository.findWordByIdForContentCreation(id);
    }

    public Optional<Word> getARandomNoun() {
        return wordRepository.getRandomNoun(random.nextLong(5_000));
    }

    private Set<Word> sortWordsFromWordFormsById(List<WordForm> wordForms) {
        try {
            Set<Word> words = new TreeSet<>(Comparator.comparing(Word::getId));

            // Add each word form to the set, the set will only keep unique values and will sort by id
            for (WordForm wordForm : wordForms) {
                words.add(wordForm.getWord());
            }

            return words;
        } catch (NullPointerException e) {
            return new HashSet<>();
        }
    }

    public WordRetrievalDTO fetchWordById(Long id) {
        try {
            WordRetrievalDTO wordDTO = null;
            Optional<Word> retrievedWord = wordRepository.findById(id);
            WordType retrievedWordType = retrievedWord.get().getType();


            return wordDTO;
        } catch (NullPointerException e) {
            WordRetrievalError error = new WordRetrievalError();
            error.setErrorMessage(e.getMessage());

            return error;
        }
    }

    public AdverbDTO getAdverbDtoByWord(Word word) {
        try {
            AdverbDTO adverbDTO = new AdverbDTO();

            adverbDTO.setBareText(word.getBare());
            adverbDTO.setAccentedText(word.getAccented());
            adverbDTO.setWordLevel(word.getWordLevel());

            // The translations are stored in the DB as an object, but we just need the string value
            for (Translation translation : word.getTranslations()) {
                adverbDTO.getTranslations().add(translation.getTranslation());
            }

            return adverbDTO;
        } catch (NullPointerException e) {
            log.error(e.getMessage());

            return AdverbDTO.getError();
        }
    }

    public OtherDTO getOtherDtoByWord(Word word) {
        try {
            OtherDTO otherDTO = new OtherDTO();

            otherDTO.setBareText(word.getBare());
            otherDTO.setAccentedText(word.getAccented());
            otherDTO.setWordLevel(word.getWordLevel());

            // The translations are stored in the DB as an object, but we just need the string value
            for (Translation translation : word.getTranslations()) {
                otherDTO.getTranslations().add(translation.getTranslation());
            }

            return otherDTO;
        } catch (NullPointerException e) {
            log.error(e.getMessage());

            return OtherDTO.getError();
        }
    }
}
