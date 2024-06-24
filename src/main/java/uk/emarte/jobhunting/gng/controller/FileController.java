package uk.emarte.jobhunting.gng.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import uk.emarte.jobhunting.gng.entity.ProcessingRecord;
import uk.emarte.jobhunting.gng.service.FileProcessingException;
import uk.emarte.jobhunting.gng.service.FileProcessingService;
import uk.emarte.jobhunting.gng.service.IFileProcessingService;
import uk.emarte.jobhunting.gng.service.IRecordService;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.*;

@Controller
@ResponseBody
public class FileController {
    private final IFileProcessingService fileProcessingService;
    private final IRecordService recordService;

    @Autowired
    public FileController(FileProcessingService fileProcessingService, IRecordService recordService) {
        this.fileProcessingService = fileProcessingService;
        this.recordService = recordService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest servletRequest) {
        String ipAddress = getClientIpAddress(servletRequest);
        ProcessingRecord record = recordService.start(ipAddress, getRequestUrl(servletRequest));
        ResponseEntity<?> responseEntity = processForResponseEntity(file, ipAddress, record);
        record.setStatusCode(String.valueOf(responseEntity.getStatusCode()));
        recordService.save(record);
        return responseEntity;
    }

    private ResponseEntity<?> processForResponseEntity(MultipartFile file, String ipAddress, ProcessingRecord record) {
        try {
            String responseJson = fileProcessingService.processFile(file, ipAddress, record);
            return new ResponseEntity<>(responseJson, jsonFileResponseHeaders(), OK);
        } catch (FileProcessingException pe) {
            return switch (pe.getProcessingError()) {
                case IP_VALIDATION_FAILURE -> ResponseEntity.status(FORBIDDEN).body(pe.getMessage());
                case FILE_VALIDATION_FAILURE -> ResponseEntity.status(BAD_REQUEST).body(pe.getMessage());
                default -> ResponseEntity.status(INTERNAL_SERVER_ERROR).body(pe.getMessage()); // TODO BREAK ERROR MESSAGE OUT INTO MULTI-FIELD ERROR RESPONSE
            };
        }
    }

    private HttpHeaders jsonFileResponseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_DISPOSITION, "attachment; filename=OutcomeFile.json");
        headers.add(CONTENT_TYPE, "application/json");
        return headers;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }

    private String getRequestUrl(HttpServletRequest request) {
        StringBuffer requestUrl = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString != null) {
            requestUrl.append("?").append(queryString);
        }

        return requestUrl.toString();
    }
}

