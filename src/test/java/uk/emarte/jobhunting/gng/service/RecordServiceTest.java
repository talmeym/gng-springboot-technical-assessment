package uk.emarte.jobhunting.gng.service;

import org.junit.jupiter.api.Test;
import uk.emarte.jobhunting.gng.entity.ProcessingRecord;
import uk.emarte.jobhunting.gng.repository.RecordRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RecordServiceTest {
    private final RecordRepository recordRepository = mock(RecordRepository.class);

    private final RecordService toTest = new RecordService(recordRepository);

    @Test
    public void testStart() {
        String ipAddress = "127.0.0.1";
        String uri = "http://localhost:8080/upload";
        ProcessingRecord record = toTest.start(ipAddress, uri);
        assertEquals(ipAddress, record.getIpAddress());
        assertEquals(uri, record.getUri());
    }

    @Test
    public void testSave() {
        ProcessingRecord record = toTest.start("127.0.0.1", "http://localhost:8080/upload");
        assertNotNull(record.getTimestamp());
        assertNull(record.getTimeLapsed());
        toTest.save(record);
        assertNotNull(record.getTimestamp());
        assertNotNull(record.getTimeLapsed());
        verify(recordRepository).save(record);
    }
}