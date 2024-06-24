package uk.emarte.jobhunting.gng.service;

import org.springframework.web.multipart.MultipartFile;
import uk.emarte.jobhunting.gng.entity.ProcessingRecord;

public interface IFileProcessingService {
    String processFile(MultipartFile file, String ipAddress, ProcessingRecord record) throws FileProcessingException;
}
