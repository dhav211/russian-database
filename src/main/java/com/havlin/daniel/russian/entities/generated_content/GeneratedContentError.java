package com.havlin.daniel.russian.entities.generated_content;

import com.havlin.daniel.russian.services.generated_content.GeneratedContentErrorOrigin;
import com.havlin.daniel.russian.services.generated_content.GeneratedContentErrorType;
import jakarta.persistence.*;

@Entity
public class GeneratedContentError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GeneratedContentErrorOrigin errorOrigin;

    @Enumerated(EnumType.STRING)
    private GeneratedContentErrorType errorType;

    private Long originatingEntityId;

    @Lob
    private String message;

    @Lob
    private String fullText;

    public Long getId() {
        return id;
    }

    public GeneratedContentErrorOrigin getErrorOrigin() {
        return errorOrigin;
    }

    public void setErrorOrigin(GeneratedContentErrorOrigin errorOrigin) {
        this.errorOrigin = errorOrigin;
    }

    public Long getOriginatingEntityId() {
        return originatingEntityId;
    }

    public void setOriginatingEntityId(Long originatingEntityId) {
        this.originatingEntityId = originatingEntityId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public GeneratedContentErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(GeneratedContentErrorType errorType) {
        this.errorType = errorType;
    }
}
