package uk.emarte.jobhunting.gng.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import uk.emarte.jobhunting.gng.entity.ProcessingRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static uk.emarte.jobhunting.gng.service.FileProcessingException.ProcessingError.*;
import static uk.emarte.jobhunting.gng.service.FileValidationException.ValidationError.OTHER_ERROR;
import static uk.emarte.jobhunting.gng.service.IpValidationException.ValidationError.API_ERROR;

class FileProcessingServiceTest {
    public static final String IP_ADDRESS = "127.0.0.1";
    public static final String SOME_CONTEXT = "Some Context";
    public static final String ENTRY_FILE_TXT = "EntryFile.txt";

    private final IIpValidationService ipValidationService = mock(IIpValidationService.class);
    private final ICsvToJsonService csvToJsonService = mock(ICsvToJsonService.class);
    private final IFileParseService fileParseService = mock(IFileParseService.class);

    private final FileProcessingService toTest = new FileProcessingService(ipValidationService, csvToJsonService, fileParseService);

    @Test
    public void testHappyPath() throws IOException, FileProcessingException, FileValidationException, IpValidationException {
        ParseResult parseResult = new ParseResult(List.of("Name"), singletonList(new String[]{"Eric"}));
        MockMultipartFile multipartFile = multipartFile(); // won't use null input stream
        String json = "[{ \"name\": \"Eric\" }]";

        when(fileParseService.parseFile(multipartFile)).thenReturn(parseResult);
        when(csvToJsonService.convertToJson(parseResult)).thenReturn(json);

        String result = toTest.processFile(multipartFile, IP_ADDRESS, new ProcessingRecord());
        assertEquals(json, result);

        verify(ipValidationService).validateIp(same(IP_ADDRESS), any(ProcessingRecord.class));
        verify(fileParseService).parseFile(multipartFile);
        verify(csvToJsonService).convertToJson(parseResult);
    }

    @Test
    public void testIpValidationException() {
        FileProcessingException exception = assertThrows(FileProcessingException.class, () -> {
            doThrow(new IpValidationException(API_ERROR, SOME_CONTEXT)).when(ipValidationService).validateIp(same(IP_ADDRESS), any(ProcessingRecord.class));
            toTest.processFile(multipartFile(), IP_ADDRESS, new ProcessingRecord());
        });

        assertSame(exception.getProcessingError(), IP_VALIDATION_FAILURE);
    }

    @Test
    public void testFileValidationException() {
        FileProcessingException exception = assertThrows(FileProcessingException.class, () -> {
            MockMultipartFile multipartFile = multipartFile();
            doThrow(new FileValidationException(OTHER_ERROR, SOME_CONTEXT)).when(fileParseService).parseFile(multipartFile);
            toTest.processFile(multipartFile, IP_ADDRESS, new ProcessingRecord());
        });

        assertSame(exception.getProcessingError(), FILE_VALIDATION_FAILURE);
    }

    @Test
    public void testIOException() {
        FileProcessingException exception = assertThrows(FileProcessingException.class, () -> {
            MockMultipartFile multipartFile = multipartFile();
            doThrow(new IOException()).when(fileParseService).parseFile(multipartFile);
            toTest.processFile(multipartFile, IP_ADDRESS, new ProcessingRecord());
        });

        assertSame(exception.getProcessingError(), OTHER_FAILURE);
    }

    private MockMultipartFile multipartFile() throws IOException {
        return new MockMultipartFile(ENTRY_FILE_TXT, (InputStream) null); // won't use null input stream
    }
}