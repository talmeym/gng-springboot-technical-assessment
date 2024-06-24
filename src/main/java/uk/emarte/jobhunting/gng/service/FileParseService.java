package uk.emarte.jobhunting.gng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileParseService implements IFileParseService {
    private final IFileValidationService fileValidationService;

    @Autowired
    public FileParseService(IFileValidationService fileValidationService) {
        this.fileValidationService = fileValidationService;
    }

    @Override
    public ParseResult parseFile(MultipartFile file) throws FileValidationException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        List<String> headers = List.of(reader.readLine().split(","));
        List<String[]> rows = reader.lines().map(line -> line.split(",")).collect(Collectors.toList());
        fileValidationService.validateFileContents(headers, rows);
        return new ParseResult(headers, rows);
    }
}
