package com.havlin.daniel.russian.services.dictionary;

public class FormTypeDoesNotHaveAGenderException extends RuntimeException {
    public FormTypeDoesNotHaveAGenderException(String formType) {
        super("Word Form doesn't have a gender, it is likely not an adjective. The form type is: " + formType);
    }
}
