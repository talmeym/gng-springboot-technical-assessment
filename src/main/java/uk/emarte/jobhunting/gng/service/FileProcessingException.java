package uk.emarte.jobhunting.gng.service;

public class FileProcessingException extends Throwable {
    private final ProcessingError ProcessingError;

    public FileProcessingException(ProcessingError ProcessingError, String context) {
        super(ProcessingError.name() + " - " + context);
        this.ProcessingError = ProcessingError;
    }

    public ProcessingError getProcessingError() {
        return ProcessingError;
    }

    public enum ProcessingError {
        IP_VALIDATION_FAILURE,
        FILE_VALIDATION_FAILURE,
        OTHER_FAILURE
    }
}
