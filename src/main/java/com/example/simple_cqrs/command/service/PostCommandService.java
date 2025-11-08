package com.example.simple_cqrs.command.service;

import com.example.simple_cqrs.command.domain.command.CreatePostCommand;
import com.example.simple_cqrs.command.domain.Post;
import com.example.simple_cqrs.command.repository.EventStoreRepository;
import com.example.simple_cqrs.shared.DomainEvent;
import com.example.simple_cqrs.shared.EventPublisher;
import com.example.simple_cqrs.shared.util.SecurityUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCommandService {

    private final EventStoreRepository eventStoreRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public UUID createPost(@Valid CreatePostCommand command) {

        Post post = Post.create(command.getContent());

        eventStoreRepository.save(post);

        // publish events for query side
        for (DomainEvent event : post.getDomainEvents()) {
            eventPublisher.publish(event);
        }

        return post.getPostId();
    }
}
