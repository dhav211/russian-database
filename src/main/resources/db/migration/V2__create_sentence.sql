CREATE TABLE IF NOT EXISTS sentence
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    word_id    BIGINT                NOT NULL,
    text       VARCHAR(255)          NULL,
    word_level SMALLINT              NULL,
    CONSTRAINT pk_sentence PRIMARY KEY (id)
);

ALTER TABLE sentence
    ADD CONSTRAINT FK_SENTENCE_ON_WORD FOREIGN KEY (word_id) REFERENCES word (id);