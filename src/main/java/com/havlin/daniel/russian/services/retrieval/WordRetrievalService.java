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

    public WordRetrievalService(WordRepository wordRepository, WordFormRepository wordFormRepository) {
        this.wordRepository = wordRepository;
        this.wordFormRepository = wordFormRepository;
    }

    public WordType getWordTypeFromWordId(Long id) {
        try {
            WordType wordType = wordRepository.findWordTypeById(id);

            return Objects.requireNonNullElse(wordType, WordType.ERROR);
        } catch (Exception e) {
            return WordType.ERROR;
        }
    }

    public Set<Word> getWordsFromBareText(String bareText) {
        List<WordForm> matchingWordForms = wordFormRepository.findAllByBare(bareText);
        return sortWordsFromWordFormsById(matchingWordForms);
    }

    public Set<Word> getWordsFromAccentedText(String accentedText) {
        List<WordForm> matchingWordsForms = wordFormRepository.findAllByAccented(accentedText);
        return sortWordsFromWordFormsById(matchingWordsForms);
    }

    private Set<Word> sortWordsFromWordFormsById(List<WordForm> wordForms) {
        Set<Word> words = new TreeSet<>(Comparator.comparing(Word::getId));

        // Add each word form to the set, the set will only keep unique values and will sort by id
        for (WordForm wordForm : wordForms) {
            words.add(wordForm.getWord());
        }

        return words;
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

    public VerbDTO getVerbDtoFromWord(Word word) {
        try {
            VerbDTO verbDTO = new VerbDTO();

            verbDTO.setBareText(word.getBare());
            verbDTO.setAccentedText(word.getAccented());
            verbDTO.setWordLevel(word.getWordLevel());

            // The translations are stored in the DB as an object, but we just need the string value
            for (Translation translation : word.getTranslations()) {
                verbDTO.getTranslations().add(translation.getTranslation());
            }

            verbDTO.setAspect(word.getVerb().getAspect());

            // We need the word object from the verb partner, for easier access later
            for (VerbPartner partner : word.getVerb().getPartners()) {
                verbDTO.getPartners().add(partner.getText());
            }

            // Set all individual word forms for the verb
            for (WordForm wordForm : word.getWordForms()) {
                switch (wordForm.getFormType()) {
                    case "ru_verb_imperative_sg":
                        verbDTO.setSingularImperative(wordForm.getAccented());
                        break;
                    case "ru_verb_imperative_pl":
                        verbDTO.setPluralImperative(wordForm.getAccented());
                        break;
                    case "ru_verb_past_m":
                        verbDTO.setMasculinePast(wordForm.getAccented());
                        break;
                    case "ru_verb_past_f":
                        verbDTO.setFemininePast(wordForm.getAccented());
                        break;
                    case "ru_verb_past_n":
                        verbDTO.setNeuterPast(wordForm.getAccented());
                        break;
                    case "ru_verb_past_pl":
                        verbDTO.setPluralPast(wordForm.getAccented());
                        break;
                    case "ru_verb_presfut_sg1":
                        verbDTO.setPresentFutureSingularFirst(wordForm.getAccented());
                        break;
                    case "ru_verb_presfut_sg2":
                        verbDTO.setPresentFutureSingularSecond(wordForm.getAccented());
                        break;
                    case "ru_verb_presfut_sg3":
                        verbDTO.setPresentFutureSingularThird(wordForm.getAccented());
                        break;
                    case "ru_verb_presfut_pl1":
                        verbDTO.setPresentFuturePluralFirst(wordForm.getAccented());
                        break;
                    case "ru_verb_presfut_pl2":
                        verbDTO.setPresentFuturePluralSecond(wordForm.getAccented());
                        break;
                    case "ru_verb_presfut_pl3":
                        verbDTO.setPresentFuturePluralThird(wordForm.getAccented());
                        break;
                    case "ru_verb_gerund_present":
                        verbDTO.setGerundPresent(wordForm.getAccented());
                        break;
                    case "ru_verb_gerund_past":
                        verbDTO.setGerundPast(wordForm.getAccented());
                        break;
                    case "ru_verb_participle_active_present":
                        verbDTO.setParticipleActivePresent(wordForm.getAccented());
                        break;
                    case "ru_verb_participle_active_past":
                        verbDTO.setParticipleActivePast(wordForm.getAccented());
                        break;
                    case "ru_verb_participle_passive_present":
                        verbDTO.setParticiplePassivePresent(wordForm.getAccented());
                        break;
                    case "ru_verb_participle_passive_past":
                        verbDTO.setParticiplePassivePast(wordForm.getAccented());
                        break;
                }
            }

            return verbDTO;
        } catch (RuntimeException e) {
            log.error(e.getMessage());

            return VerbDTO.getError();
        }
    }

    public NounDTO getNounDtoFromWord(Word word) {
        try {
            NounDTO nounDTO = new NounDTO();

            nounDTO.setBareText(word.getBare());
            nounDTO.setAccentedText(word.getAccented());
            nounDTO.setWordLevel(word.getWordLevel());

            // The translations are stored in the DB as an object, but we just need the string value
            for (Translation translation : word.getTranslations()) {
                nounDTO.getTranslations().add(translation.getTranslation());
            }

            for (WordForm wordForm : word.getWordForms()) {
                switch (wordForm.getFormType()) {
                    case "ru_noun_sg_nom":
                        nounDTO.setNominativeSingular(wordForm.getAccented());
                        break;
                    case "ru_noun_sg_gen":
                        nounDTO.setGenitiveSingular(wordForm.getAccented());
                        break;
                    case "ru_noun_sg_dat":
                        nounDTO.setDativeSingular(wordForm.getAccented());
                        break;
                    case "ru_noun_sg_acc":
                        nounDTO.setAccusativeSingular(wordForm.getAccented());
                        break;
                    case "ru_noun_sg_inst":
                        nounDTO.setInstrumentalSingular(wordForm.getAccented());
                        break;
                    case "ru_noun_sg_prep":
                        nounDTO.setPrepositionalSingular(wordForm.getAccented());
                        break;
                    case "ru_noun_pl_nom":
                        nounDTO.setNominativePlural(wordForm.getAccented());
                        break;
                    case "ru_noun_pl_gen":
                        nounDTO.setGenitivePlural(wordForm.getAccented());
                        break;
                    case "ru_noun_pl_dat":
                        nounDTO.setDativePlural(wordForm.getAccented());
                        break;
                    case "ru_noun_pl_acc":
                        nounDTO.setAccusativePlural(wordForm.getAccented());
                        break;
                    case "ru_noun_pl_inst":
                        nounDTO.setInstrumentalPlural(wordForm.getAccented());
                        break;
                    case "ru_noun_pl_prep":
                        nounDTO.setPrepositionalPlural(wordForm.getAccented());
                        break;
                }
            }

            return nounDTO;
        } catch (RuntimeException e) {
            log.error(e.getMessage());

            return NounDTO.getError();
        }
    }

    public AdjectiveDTO getAdjectiveDtoFromWord(Word word) {
        try {
            AdjectiveDTO adjectiveDTO = new AdjectiveDTO();

            adjectiveDTO.setBareText(word.getBare());
            adjectiveDTO.setAccentedText(word.getAccented());
            adjectiveDTO.setWordLevel(word.getWordLevel());

            // The translations are stored in the DB as an object, but we just need the string value
            for (Translation translation : word.getTranslations()) {
                adjectiveDTO.getTranslations().add(translation.getTranslation());
            }

            for (WordForm wordForm : word.getWordForms()) {
                switch (wordForm.getFormType()) {
                    case "ru_adj_comparative":
                        adjectiveDTO.setComparative(wordForm.getAccented());
                        break;
                    case "ru_adj_superlative":
                        adjectiveDTO.setSuperlative(wordForm.getAccented());
                        break;
                    case "ru_adj_short_m":
                        adjectiveDTO.setMasculineShort(wordForm.getAccented());
                        break;
                    case "ru_adj_short_f":
                        adjectiveDTO.setFeminineShort(wordForm.getAccented());
                        break;
                    case "ru_adj_short_n":
                        adjectiveDTO.setNeuterShort(wordForm.getAccented());
                        break;
                    case "ru_adj_short_pl":
                        adjectiveDTO.setPluralShort(wordForm.getAccented());
                        break;
                    case "ru_adj_m_nom":
                        adjectiveDTO.setMasculineNominative(wordForm.getAccented());
                        break;
                    case "ru_adj_m_gen":
                        adjectiveDTO.setMasculineGenitive(wordForm.getAccented());
                        break;
                    case "ru_adj_m_dat":
                        adjectiveDTO.setMasculineDative(wordForm.getAccented());
                        break;
                    case "ru_adj_m_acc":
                        adjectiveDTO.setMasculineAccusative(wordForm.getAccented());
                        break;
                    case "ru_adj_m_inst":
                        adjectiveDTO.setMasculineInstrumental(wordForm.getAccented());
                        break;
                    case "ru_adj_m_prep":
                        adjectiveDTO.setMasculinePrepositional(wordForm.getAccented());
                        break;
                    case "ru_adj_f_nom":
                        adjectiveDTO.setFeminineNominative(wordForm.getAccented());
                        break;
                    case "ru_adj_f_gen":
                        adjectiveDTO.setFeminineGenitive(wordForm.getAccented());
                        break;
                    case "ru_adj_f_dat":
                        adjectiveDTO.setFeminineDative(wordForm.getAccented());
                        break;
                    case "ru_adj_f_acc":
                        adjectiveDTO.setFeminineAccusative(wordForm.getAccented());
                        break;
                    case "ru_adj_f_inst":
                        // There can be multiple entries in this case, here we will display them with a comma in between
                        if (adjectiveDTO.getFeminineInstrumental() == null) {
                            adjectiveDTO.setFeminineInstrumental(wordForm.getAccented());
                        } else {
                            // TODO this should be done when database is created
                            // Remove the parenthesis from the word.
                            if (wordForm.getAccented().contains("(")) {
                                StringBuilder stringBuilder = new StringBuilder(wordForm.getAccented());
                                stringBuilder.deleteCharAt(0);
                                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                                wordForm.setAccented(stringBuilder.toString());
                            }
                            adjectiveDTO.setFeminineInstrumental(adjectiveDTO.getFeminineInstrumental() + ", " + wordForm.getAccented());
                        }
                        break;
                    case "ru_adj_f_prep":
                        adjectiveDTO.setFemininePrepositional(wordForm.getAccented());
                        break;
                    case "ru_adj_n_nom":
                        adjectiveDTO.setNeuterNominative(wordForm.getAccented());
                        break;
                    case "ru_adj_n_gen":
                        adjectiveDTO.setNeuterGenitive(wordForm.getAccented());
                        break;
                    case "ru_adj_n_dat":
                        adjectiveDTO.setNeuterDative(wordForm.getAccented());
                        break;
                    case "ru_adj_n_acc":
                        adjectiveDTO.setNeuterAccusative(wordForm.getAccented());
                        break;
                    case "ru_adj_n_inst":
                        adjectiveDTO.setNeuterInstrumental(wordForm.getAccented());
                        break;
                    case "ru_adj_n_prep":
                        adjectiveDTO.setNeuterPrepositional(wordForm.getAccented());
                        break;
                    case "ru_adj_pl_nom":
                        adjectiveDTO.setPluralNominative(wordForm.getAccented());
                        break;
                    case "ru_adj_pl_gen":
                        adjectiveDTO.setPluralGenitive(wordForm.getAccented());
                        break;
                    case "ru_adj_pl_dat":
                        adjectiveDTO.setPluralDative(wordForm.getAccented());
                        break;
                    case "ru_adj_pl_acc":
                        adjectiveDTO.setPluralAccusative(wordForm.getAccented());
                        break;
                    case "ru_adj_pl_inst":
                        adjectiveDTO.setPluralInstrumental(wordForm.getAccented());
                        break;
                    case "ru_adj_pl_prep":
                        adjectiveDTO.setPluralPrepositional(wordForm.getAccented());
                        break;
                }
            }
            return adjectiveDTO;
        } catch (RuntimeException e) {
            log.error(e.getMessage());

            return AdjectiveDTO.getError();
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
