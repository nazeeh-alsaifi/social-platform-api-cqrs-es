package com.example.simple_cqrs.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
