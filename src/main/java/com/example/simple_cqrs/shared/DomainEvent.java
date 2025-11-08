package com.example.simple_cqrs.shared;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

    UUID getEventId();

    UUID getAggregateId();

    String getEventType();

    Instant getTimestamp();

    String getUserId();
}
