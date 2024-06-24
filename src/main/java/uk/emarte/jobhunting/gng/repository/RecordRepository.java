package uk.emarte.jobhunting.gng.repository;

import org.springframework.data.repository.ListCrudRepository;
import uk.emarte.jobhunting.gng.entity.ProcessingRecord;

import java.util.UUID;

public interface RecordRepository extends ListCrudRepository<ProcessingRecord, UUID> {
}

