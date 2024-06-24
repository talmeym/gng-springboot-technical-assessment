package uk.emarte.jobhunting.gng.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static uk.emarte.jobhunting.gng.service.FileValidationException.ValidationError.INVALID_HEADERS;
import static uk.emarte.jobhunting.gng.service.FileValidationException.ValidationError.INVALID_ROW_DATA;

@Service
public class FileValidationService implements IFileValidationService {
    private final Boolean validateContents;
    private final List<String> expectedHeaders;

    public FileValidationService(@Value("${file.validation.enabled}") Boolean validateContents, @Value("${file.validation.expected.headers}") String expectedHeaders) {
        this.validateContents = validateContents;
        this.expectedHeaders = List.of(expectedHeaders.split(","));
    }

    @Override
    public void validateFileContents(List<String> headers, List<String[]> rows) throws FileValidationException {
        if(validateContents) {
            if (!headers.equals(expectedHeaders)) {
                throw new FileValidationException(INVALID_HEADERS, "FILE ROW 1");
            }

            if (!rows.stream().allMatch(r -> r.length == expectedHeaders.size())) {
                int row = rows.indexOf(rows.stream().filter(r -> r.length != expectedHeaders.size()).findFirst().get()) + 2;
                // +2 = INDEX PLUS ONE PLUS HEADER ROW
                throw new FileValidationException(INVALID_ROW_DATA, "FILE ROW " + row);
            }
        }
    }
}
