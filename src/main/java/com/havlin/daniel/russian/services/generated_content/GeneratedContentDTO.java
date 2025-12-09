package com.havlin.daniel.russian.services.generated_content;

import com.havlin.daniel.russian.entities.dictionary.Word;
import com.havlin.daniel.russian.entities.generated_content.Definition;
import com.havlin.daniel.russian.entities.generated_content.Sentence;

import java.util.List;
import java.util.Set;

/**
 * This is the corrected generated content from the LLM, it has been successfully turned into savable entities to the
 * database. It contains all the sentences, definitions, word information, and also the words used in the sentences that
 * will be resaved into the database.
 */
record GeneratedContentDTO(Set<Sentence> sentences, Set<Definition> definitions, Set<Word> words) {}
