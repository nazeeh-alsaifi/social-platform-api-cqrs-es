package com.example.simple_cqrs.command;

import com.example.simple_cqrs.command.domain.command.CreatePostCommand;
import com.example.simple_cqrs.command.domain.Post;
import com.example.simple_cqrs.command.domain.event.PostCreatedEvent;
import com.example.simple_cqrs.command.repository.EventStoreRepository;
import com.example.simple_cqrs.command.service.PostCommandService;
import com.example.simple_cqrs.shared.EventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostCommandServiceTest {
    @Mock
    private EventStoreRepository eventStoreRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private PostCommandService postCommandService;

    @Test
    void givenAuthUser_whenCreatePost_thenCreatePostIdAndPublishEvent() {
        // Arrange
        CreatePostCommand command = new CreatePostCommand("Test content");

        // Act
        UUID postId = postCommandService.createPost(command);

        // Assert
        assertNotNull(postId);
        verify(eventStoreRepository, times(1)).save(any(Post.class));
        verify(eventPublisher, times(1)).publish(any(PostCreatedEvent.class));
    }
}
