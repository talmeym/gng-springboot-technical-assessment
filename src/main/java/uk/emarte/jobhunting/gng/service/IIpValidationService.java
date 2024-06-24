package uk.emarte.jobhunting.gng.service;

import uk.emarte.jobhunting.gng.entity.ProcessingRecord;

public interface IIpValidationService {
    void validateIp(String ipAddress, ProcessingRecord record) throws IpValidationException;
}
