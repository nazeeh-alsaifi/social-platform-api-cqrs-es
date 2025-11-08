package com.example.simple_cqrs.command.repository;

import com.example.simple_cqrs.command.domain.EventStoreEntry;
import com.example.simple_cqrs.command.domain.Post;
import com.example.simple_cqrs.command.domain.PostStatus;
import com.example.simple_cqrs.shared.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EventStoreRepository {
    private final ObjectMapper objectMapper;
    private final EventStoreJpaRepository eventStoreJpaRepository;


    public void save(Post post) {
        List<DomainEvent> events = post.getDomainEvents();

        for (DomainEvent event : events) {
            String eventData = serializeEvent(event);

            EventStoreEntry entry = new EventStoreEntry(
                    event.getEventId(),
                    event.getAggregateId(),
                    "Post",
                    event.getEventType(),
                    eventData,
                    post.getVersion(),
                    event.getTimestamp(),
                    event.getUserId()
            );

            eventStoreJpaRepository.save(entry);
        }

    }

    public Optional<Post> findById(UUID postId) {
        List<EventStoreEntry> rows = eventStoreJpaRepository.findByAggregateIdOrderByVersionAsc(postId);

        if (rows.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(rebuildFromEvents(rows));
    }

    private String serializeEvent(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }


    private Post rebuildFromEvents(List<EventStoreEntry> rows) {
        Post post = new Post();

        for (EventStoreEntry row : rows) {
            String eventType = row.getEventType();
            String eventData = row.getEventData();

            switch (eventType) {
                case "PostCreatedEvent":
                    applyPostCreatedEvent(post, eventData);
                    break;
                case "PostApprovedEvent":
                    applyPostApprovedEvent(post);
                    break;
                case "PostRejectedEvent":
                    applyPostRejectedEvent(post);
                    break;
                case "PostLikedEvent":
                    applyPostLikedEvent(post, row.getUserId());
                    break;
            }
        }

        return post;
    }

    private void applyPostLikedEvent(Post post, String userId) {
        post.setVersion(post.getVersion() + 1);
        post.getLikedUsers().add(userId);
    }

    private void applyPostRejectedEvent(Post post) {
        post.setStatus(PostStatus.REJECTED);
        post.setVersion(post.getVersion() + 1);
    }

    private void applyPostApprovedEvent(Post post) {
        post.setStatus(PostStatus.APPROVED);
        post.setVersion(post.getVersion() + 1);
    }

    private void applyPostCreatedEvent(Post post, String eventData) {
        try {
            Map<String, Object> data = objectMapper.readValue(eventData, Map.class);

            post.setPostId(UUID.fromString(String.valueOf(data.get("aggregateId"))));
            post.setUserId(String.valueOf(data.get("userId")));
            post.setContent(String.valueOf(data.get("content")));
            post.setStatus(PostStatus.PENDING);
            post.setCreatedAt(Instant.parse(String.valueOf(data.get("timestamp"))));
            post.setVersion(0);
        } catch (Exception e) {
            throw new RuntimeException("failed to apply PostCreatedEvent", e);
        }
    }
}
