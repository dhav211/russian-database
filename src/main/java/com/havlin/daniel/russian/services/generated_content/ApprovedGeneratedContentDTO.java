package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.generated_content.Sentence;
import com.havlin.daniel.russian.entities.generated_content.WordInformation;

import java.util.Set;

/**
 * Completely converted generated data from the LLM, mostly corrected and converted into entities that will be saved to
 * the database
 * @param sentences All approved sentences
 * @param definitions All approved definitions
 * @param wordInformation The word information, that hasn't been correct but will be trusted
 * @param words The unique words that have been found in the sentences, these will need to be saved to database
 */
record ApprovedGeneratedContent(Set<Sentence> sentences, Set<Definition> definitions,
                                   WordInformation wordInformation, Set<Word> words) {
}
