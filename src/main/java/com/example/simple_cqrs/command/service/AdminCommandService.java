package com.example.simple_cqrs.command.service;

import com.example.simple_cqrs.command.domain.Post;
import com.example.simple_cqrs.command.domain.command.ApprovePostCommand;
import com.example.simple_cqrs.command.domain.command.RejectPostCommand;
import com.example.simple_cqrs.command.repository.EventStoreRepository;
import com.example.simple_cqrs.shared.DomainEvent;
import com.example.simple_cqrs.shared.EventPublisher;
import com.example.simple_cqrs.shared.exception.PostConcurrentModificationException;
import com.example.simple_cqrs.shared.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCommandService {

    private final EventStoreRepository eventStoreRepository;
    private final EventPublisher eventPublisher;

    public void approvePost(ApprovePostCommand command) {
        Post post = eventStoreRepository.findById(command.getPostId())
                .orElseThrow(() -> new PostNotFoundException("post not found: " + command.getPostId()));

        post.approve();

        try {
            eventStoreRepository.save(post);
        } catch (DataIntegrityViolationException exception) {
            throw new PostConcurrentModificationException();
        }

        // publish events for query side
        for (DomainEvent event : post.getDomainEvents()) {
            eventPublisher.publish(event);
        }
    }


    public void rejectPost(RejectPostCommand command) {
        Post post = eventStoreRepository.findById(command.getPostId())
                .orElseThrow(() -> new PostNotFoundException("post not found: " + command.getPostId()));

        post.reject(command.getReason());

        try {
            eventStoreRepository.save(post);
        } catch (DataIntegrityViolationException exception) {
            throw new PostConcurrentModificationException();
        }

        // publish events for query side
        for (DomainEvent event : post.getDomainEvents()) {
            eventPublisher.publish(event);
        }
    }
}
