CREATE TABLE IF NOT EXISTS word_sentence
(
    sentence_id BIGINT NOT NULL,
    word_id     BIGINT NOT NULL,
    CONSTRAINT pk_word_sentence PRIMARY KEY (sentence_id, word_id)
);

ALTER TABLE word_sentence
    ADD CONSTRAINT fk_worsen_on_sentence FOREIGN KEY (sentence_id) REFERENCES sentence (id);

ALTER TABLE word_sentence
    ADD CONSTRAINT fk_worsen_on_word FOREIGN KEY (word_id) REFERENCES word (id);