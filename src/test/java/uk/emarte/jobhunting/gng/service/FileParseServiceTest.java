package uk.emarte.jobhunting.gng.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.emarte.jobhunting.gng.service.FileValidationException.ValidationError.OTHER_ERROR;

class FileParseServiceTest {
    private final IFileValidationService fileValidationService = Mockito.mock(IFileValidationService.class);

    private final FileParseService toTest = new FileParseService(fileValidationService);

    @Test
    public void testValidFileParse() throws IOException, FileValidationException {
        MockMultipartFile multipartFile = new MockMultipartFile("EntryFile.txt", FileParseServiceTest.class.getResourceAsStream("/EntryFile.txt"));
        ParseResult parseResult = toTest.parseFile(multipartFile);
        assertEquals(3, parseResult.getRowCount());
        assertEquals("John Smith", parseResult.getValue(0, "Name"));
        assertEquals("Likes Grape", parseResult.getValue(1, "Likes"));
        assertEquals("15.3", parseResult.getValue(2, "Top Speed"));
    }

    @Test
    public void testValidationFailureBubblesUpException() throws FileValidationException {
        Mockito.doThrow(new FileValidationException(OTHER_ERROR, "Test Error")).when(fileValidationService).validateFileContents(Mockito.any(), Mockito.any());

        assertThrows(FileValidationException.class, () -> {
            // valid file but forced validation error
            InputStream resourceAsStream = FileParseServiceTest.class.getResourceAsStream("/EntryFile.txt");
            toTest.parseFile(new MockMultipartFile("EntryFile.txt", resourceAsStream));
        });
    }
}