package uk.emarte.jobhunting.gng.service;

public class FileValidationException extends Exception {
    private final ValidationError validationError;

    public FileValidationException(ValidationError validationError, String context) {
        super(validationError.name() + " - " + context);
        this.validationError = validationError;
    }

    public ValidationError getValidationError() {
        return validationError;
    }

    public enum ValidationError {
        INVALID_HEADERS,
        INVALID_ROW_DATA,
        OTHER_ERROR
    }
}
