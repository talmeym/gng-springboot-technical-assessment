package uk.emarte.jobhunting.gng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.emarte.jobhunting.gng.entity.ProcessingRecord;

import java.io.IOException;

import static uk.emarte.jobhunting.gng.service.FileProcessingException.ProcessingError.*;

@Service
public class FileProcessingService implements IFileProcessingService {
    private final IIpValidationService ipValidationService;
    private final ICsvToJsonService csvToJsonService;
    private final IFileParseService fileParseService;

    @Autowired
    public FileProcessingService(IIpValidationService ipValidationService, ICsvToJsonService csvToJsonService, IFileParseService fileParseService) {
        this.ipValidationService = ipValidationService;
        this.csvToJsonService = csvToJsonService;
        this.fileParseService = fileParseService;
    }

    public String processFile(MultipartFile file, String ipAddress, ProcessingRecord record) throws FileProcessingException {
        try {
            ipValidationService.validateIp(ipAddress, record);
            return csvToJsonService.convertToJson(fileParseService.parseFile(file));
        } catch (IpValidationException ive) {
            throw new FileProcessingException(IP_VALIDATION_FAILURE, ive.getMessage());
        } catch (FileValidationException fve) {
            throw new FileProcessingException(FILE_VALIDATION_FAILURE, fve.getMessage());
        } catch (IOException ioe) {
            throw new FileProcessingException(OTHER_FAILURE, ioe.getMessage());
        }
    }
}
