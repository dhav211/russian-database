package com.havlin.daniel.russian.controllers.retrieval;

import com.havlin.daniel.russian.entities.retrieval.ClickedWordDTO;
import com.havlin.daniel.russian.services.dictionary.WordService;
import com.havlin.daniel.russian.services.retrieval.WordRetrievalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Set;

@RestController
public class WordRetrievalController {
    private static final Logger log = LoggerFactory.getLogger(WordRetrievalController.class);
    private final WordRetrievalService wordRetrievalService;

    public WordRetrievalController(WordRetrievalService wordRetrievalService) {
        this.wordRetrievalService = wordRetrievalService;
    }

    @GetMapping("/findClickedWord/{word}")
    public ResponseEntity<Set<ClickedWordDTO>> findClickedWord(@PathVariable  String word) {
        try {
        Set<ClickedWordDTO> clickedWords = wordRetrievalService.getClickedWord(word);

        if (clickedWords.isEmpty()) {
            // use an api to find english translation
            clickedWords.add(ClickedWordDTO.translation("translation"));
        }

        return ResponseEntity.ok(clickedWords);
        } catch(NullPointerException | HttpMessageConversionException e ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Set.of(ClickedWordDTO.error(e.getMessage())));
        }
    }
}
