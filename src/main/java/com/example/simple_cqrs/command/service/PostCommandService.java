package com.example.simple_cqrs.command.service;

import com.example.simple_cqrs.command.domain.Post;
import com.example.simple_cqrs.command.domain.command.CreatePostCommand;
import com.example.simple_cqrs.command.domain.command.LikePostCommand;
import com.example.simple_cqrs.command.repository.EventStoreRepository;
import com.example.simple_cqrs.shared.DomainEvent;
import com.example.simple_cqrs.shared.EventPublisher;
import com.example.simple_cqrs.shared.exception.PostConcurrentModificationException;
import com.example.simple_cqrs.shared.exception.PostNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    public UUID likePost(@Valid LikePostCommand command) {
        Post post = eventStoreRepository.findById(command.getPostId())
                .orElseThrow(() -> new PostNotFoundException("post not found: " + command.getPostId()));

        post.like();

        try {
            eventStoreRepository.save(post);
        } catch (DataIntegrityViolationException exception) {
            throw new PostConcurrentModificationException();
        }

        // publish events for query side
        for (DomainEvent event : post.getDomainEvents()) {
            eventPublisher.publish(event);
        }

        return post.getPostId();

    }
}
