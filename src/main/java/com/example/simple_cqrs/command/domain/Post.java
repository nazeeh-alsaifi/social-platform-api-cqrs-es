package com.example.simple_cqrs.command.domain;

import com.example.simple_cqrs.command.domain.event.PostApprovedEvent;
import com.example.simple_cqrs.command.domain.event.PostCreatedEvent;
import com.example.simple_cqrs.command.domain.event.PostLikedEvent;
import com.example.simple_cqrs.command.domain.event.PostRejectedEvent;
import com.example.simple_cqrs.shared.DomainEvent;
import com.example.simple_cqrs.shared.exception.CustomValidationException;
import com.example.simple_cqrs.shared.util.SecurityUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
public class Post {

    private UUID postId;
    private String userId;
    private String content;
    private PostStatus status;
    private String rejectionReason;
    private Instant createdAt;
    private int version;

    private Set<String> likedUsers = new HashSet<>();

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public static Post create(String content) {
        Post post = new Post();
        post.postId = UUID.randomUUID();
        post.userId = SecurityUtils.getCurrentUsername();
        post.content = content;
        post.status = PostStatus.PENDING;
        post.createdAt = Instant.now();
        post.version = 0;

        post.addEvent(new PostCreatedEvent(UUID.randomUUID(), post.postId, post.userId, post.content, post.createdAt));

        return post;
    }

    private void addEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    private void clearAndAddEvent(DomainEvent event) {
        domainEvents.clear();
        domainEvents.add(event);
    }

    public void like(){
        if(likedUsers.contains(SecurityUtils.getCurrentUsername())){
            throw new CustomValidationException("already liked the post");

        }
        this.version++;
        addEvent(new PostLikedEvent(UUID.randomUUID(), this.postId, SecurityUtils.getCurrentUsername(),
                Instant.now()));

    }


    public void approve() {
        if (this.status != PostStatus.PENDING) {
            throw new CustomValidationException("only pending posts can be approved");
        }

        this.status = PostStatus.APPROVED;
        this.version++;

        addEvent(new PostApprovedEvent(UUID.randomUUID(), this.postId, SecurityUtils.getCurrentUsername(),
                Instant.now()));
    }

    public void reject( String reason) {
        if (this.status != PostStatus.PENDING) {
            throw new CustomValidationException("only pending posts can be rejected");
        }

        this.status = PostStatus.REJECTED;
        this.version++;

        addEvent(new PostRejectedEvent(UUID.randomUUID(), this.postId, SecurityUtils.getCurrentUsername(),
                Instant.now(), reason));
    }
}