package uk.emarte.jobhunting.gng.service;

public class IpValidationException extends Exception {
    private final ValidationError validationError;

    public IpValidationException(ValidationError validationError, String context) {
        super(validationError.name() + " - " + context);

        this.validationError = validationError;
    }

    public ValidationError getValidationError() {
        return validationError;
    }

    public enum ValidationError {
        BLOCKED_COUNTRY_CODE,
        BLOCKED_ISP,
        API_ERROR
    }
}
