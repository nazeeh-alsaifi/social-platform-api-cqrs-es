package com.example.simple_cqrs.command.domain.event;

import com.example.simple_cqrs.shared.DomainEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PostCreatedEvent implements DomainEvent {
    private UUID eventId;
    private UUID aggregateId;
    private String userId;
    private String content;
    private Instant timestamp;

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getEventType() {
        return "PostCreatedEvent";
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
