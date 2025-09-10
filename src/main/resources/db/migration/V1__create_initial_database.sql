CREATE TABLE IF NOT EXISTS `definition`
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    short_definition LONGTEXT              NULL,
    long_definition  LONGTEXT              NULL,
    CONSTRAINT pk_definition PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS noun
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    gender        VARCHAR(255)          NULL,
    partner       VARCHAR(255)          NULL,
    animate       BIT(1)                NOT NULL,
    indeclinable  BIT(1)                NOT NULL,
    singular_only BIT(1)                NOT NULL,
    plural_only   BIT(1)                NOT NULL,
    CONSTRAINT pk_noun PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS translation
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    word_id     BIGINT                NOT NULL,
    translation VARCHAR(255)          NULL,
    CONSTRAINT pk_translation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS verb
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    aspect VARCHAR(255)          NULL,
    CONSTRAINT pk_verb PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS verb_partner
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    verb_id BIGINT                NOT NULL,
    text    VARCHAR(255)          NULL,
    CONSTRAINT pk_verbpartner PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS word
(
    id            BIGINT       NOT NULL,
    bare          VARCHAR(255) NULL,
    accented      VARCHAR(255) NULL,
    type          VARCHAR(255) NULL,
    word_level    VARCHAR(255) NULL,
    noun_id       BIGINT       NULL,
    verb_id       BIGINT       NULL,
    definition_id BIGINT       NULL,
    CONSTRAINT pk_word PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS word_form
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    word_id   BIGINT                NOT NULL,
    form_type VARCHAR(255)          NULL,
    bare      VARCHAR(255)          NULL,
    accented  VARCHAR(255)          NULL,
    CONSTRAINT pk_wordform PRIMARY KEY (id)
);

ALTER TABLE word
    ADD CONSTRAINT uc_word_definition UNIQUE (definition_id);

ALTER TABLE word
    ADD CONSTRAINT uc_word_noun UNIQUE (noun_id);

ALTER TABLE word
    ADD CONSTRAINT uc_word_verb UNIQUE (verb_id);

ALTER TABLE translation
    ADD CONSTRAINT FK_TRANSLATION_ON_WORD FOREIGN KEY (word_id) REFERENCES word (id);

ALTER TABLE verb_partner
    ADD CONSTRAINT FK_VERBPARTNER_ON_VERB FOREIGN KEY (verb_id) REFERENCES verb (id);

ALTER TABLE word_form
    ADD CONSTRAINT FK_WORDFORM_ON_WORD FOREIGN KEY (word_id) REFERENCES word (id);

ALTER TABLE word
    ADD CONSTRAINT FK_WORD_ON_DEFINITION FOREIGN KEY (definition_id) REFERENCES `definition` (id);

ALTER TABLE word
    ADD CONSTRAINT FK_WORD_ON_NOUN FOREIGN KEY (noun_id) REFERENCES noun (id);

ALTER TABLE word
    ADD CONSTRAINT FK_WORD_ON_VERB FOREIGN KEY (verb_id) REFERENCES verb (id);