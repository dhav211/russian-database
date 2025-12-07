package com.havlin.daniel.russian.services.dictionary;

public class FormTypeDoesNotHaveACaseException extends RuntimeException {
    public FormTypeDoesNotHaveACaseException(String formType) {
        super("Word Form doesn't have a case, it is likely not a noun. The form type is: " + formType);
    }
}
