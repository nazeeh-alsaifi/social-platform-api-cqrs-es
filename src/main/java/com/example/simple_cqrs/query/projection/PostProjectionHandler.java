package com.example.simple_cqrs.query.projection;

import com.example.simple_cqrs.command.domain.event.PostApprovedEvent;
import com.example.simple_cqrs.command.domain.event.PostCreatedEvent;
import com.example.simple_cqrs.command.domain.PostStatus;
import com.example.simple_cqrs.command.domain.event.PostLikedEvent;
import com.example.simple_cqrs.command.domain.event.PostRejectedEvent;
import com.example.simple_cqrs.query.model.PostView;
import com.example.simple_cqrs.query.repository.PostViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostProjectionHandler {

    private final PostViewRepository postViewRepository;

    @EventListener
    public void handlePostCreated(PostCreatedEvent event){
        log.info("handling PostCreatedEvent for postId: {}", event.getAggregateId());

        PostView postView = new PostView(
                event.getAggregateId(),
                event.getUserId(),
                event.getUserId(), // In real scenario, fetch display name from user service or cache
                event.getContent(),
                PostStatus.PENDING,
                event.getTimestamp()
        );

        postViewRepository.save(postView);
        log.info("created PostView for postId: {}", event.getAggregateId());
    }

    @EventListener
    public void handlePostApproved(PostApprovedEvent event){
        log.info("handling PostApproved for postId: {}", event.getAggregateId());

        postViewRepository.findById(event.getAggregateId()).ifPresent(postView -> {
            postView.setStatus(PostStatus.APPROVED);
            postView.setApprovedAt(event.getTimestamp());
            postViewRepository.save(postView);
            log.info("updated PostView status to Approved for postId: {}", event.getAggregateId());
        });
    }

    @EventListener
    public void handlePostRejected(PostRejectedEvent event){
        log.info("handling PostRejected for postId: {}", event.getAggregateId());

        postViewRepository.findById(event.getAggregateId()).ifPresent(postView -> {
            postView.setStatus(PostStatus.REJECTED);
            postView.setRejectionReason(event.getRejectionReason());
            postView.setRejectedAt(event.getTimestamp());
            postViewRepository.save(postView);
            log.info("updated PostView status to Rejected for postId: {}", event.getAggregateId());
        });
    }

    @EventListener
    public void handlePostLiked(PostLikedEvent event){
        log.info("handling PostLiked for postId: {}", event.getAggregateId());

        postViewRepository.findById(event.getAggregateId()).ifPresent(postView -> {
            postView.setStatus(PostStatus.REJECTED);
            postView.setRejectedAt(event.getTimestamp());
            postView.setLikeCount(postView.getLikeCount() + 1 );
            postViewRepository.save(postView);
            log.info("updated PostView status to liked for postId: {}", event.getAggregateId());
        });
    }
}
