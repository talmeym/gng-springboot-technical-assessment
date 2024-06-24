package uk.emarte.jobhunting.gng.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static uk.emarte.jobhunting.gng.service.FileValidationException.ValidationError.INVALID_HEADERS;
import static uk.emarte.jobhunting.gng.service.FileValidationException.ValidationError.INVALID_ROW_DATA;

class FileValidationServiceTest {
    private final FileValidationService toTestValidating = new FileValidationService(true, "Name,Age");
    private final FileValidationService toTestNotValidating = new FileValidationService(false, "Name,Age");

    @Test
    public void testValidatingHappyPath() throws FileValidationException {
        toTestValidating.validateFileContents(List.of("Name", "Age"), singletonList(new String[]{"Eric", "28"}));
    }

    @Test
    public void testNotValidatingHappyPath() throws FileValidationException {
        toTestNotValidating.validateFileContents(List.of("Name"), singletonList(new String[]{"Eric"}));
    }

    @Test
    public void testInvalidHeaders() {
        FileValidationException exception = assertThrows(FileValidationException.class, () -> toTestValidating.validateFileContents(List.of("Name"), singletonList(new String[]{"Eric", "28"})));
        assertSame(exception.getValidationError(), INVALID_HEADERS);
    }

    @Test
    public void testInvalidRows() {
        FileValidationException exception = assertThrows(FileValidationException.class, () -> toTestValidating.validateFileContents(List.of("Name", "Age"), singletonList(new String[]{"Eric"})));
        assertSame(exception.getValidationError(), INVALID_ROW_DATA);
    }
}