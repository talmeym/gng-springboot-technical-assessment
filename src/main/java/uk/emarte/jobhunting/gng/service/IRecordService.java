package uk.emarte.jobhunting.gng.service;

import uk.emarte.jobhunting.gng.entity.ProcessingRecord;

public interface IRecordService {
    ProcessingRecord start(String ipAddress, String uri);

    void save(ProcessingRecord record);
}

