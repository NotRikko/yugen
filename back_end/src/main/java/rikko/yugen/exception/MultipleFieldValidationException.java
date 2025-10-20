package rikko.yugen.exception;

import java.util.Map;

public class MultipleFieldValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public MultipleFieldValidationException(Map<String, String> errors) {
        super("Multiple field validation errors");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}