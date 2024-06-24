package uk.emarte.jobhunting.gng.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileParseService {
    ParseResult parseFile(MultipartFile file) throws FileValidationException, IOException;
}
