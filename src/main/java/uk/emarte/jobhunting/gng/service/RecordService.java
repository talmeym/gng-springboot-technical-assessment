package uk.emarte.jobhunting.gng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.emarte.jobhunting.gng.entity.ProcessingRecord;
import uk.emarte.jobhunting.gng.repository.RecordRepository;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.NANOS;

@Service
public class RecordService implements IRecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public ProcessingRecord start(String ipAddress, String uri) {
        ProcessingRecord record = new ProcessingRecord();
        record.setTimestamp(now());
        record.setIpAddress(ipAddress);
        record.setUri(uri);
        return record;
    }

    @Override
    public void save(ProcessingRecord record) {
        LocalDateTime started = record.getTimestamp();
        record.setTimeLapsed(NANOS.between(started, now())); // TODO
        recordRepository.save(record);
    }
}
