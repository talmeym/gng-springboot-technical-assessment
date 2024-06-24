package uk.emarte.jobhunting.gng.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Data
public class ProcessingRecord {
    @Id
    @GeneratedValue(strategy = UUID)
    private UUID id;
    private String uri;
    private LocalDateTime timestamp;
    private String statusCode;
    private String ipAddress;
    private String countryCode;
    private String ipProvider;
    private Long timeLapsed;
}
