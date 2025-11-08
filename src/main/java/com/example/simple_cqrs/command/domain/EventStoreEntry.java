package com.example.simple_cqrs.command.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event_store", indexes = {
        @Index(name = "idx_aggregate_id", columnList = "aggregateId"),
        @Index(name = "idx_timestamp", columnList = "timestamp"),
        @Index(name = "idx_event_type", columnList = "eventType")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"aggregateId", "version"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventStoreEntry {

    @Id
    private UUID eventId;

    @Column(nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String eventData;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private String userId;
}
