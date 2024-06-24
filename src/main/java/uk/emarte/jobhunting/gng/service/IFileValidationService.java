package uk.emarte.jobhunting.gng.service;

import java.util.List;

public interface IFileValidationService {
    void validateFileContents(List<String> headers, List<String[]> rows) throws FileValidationException;
}
