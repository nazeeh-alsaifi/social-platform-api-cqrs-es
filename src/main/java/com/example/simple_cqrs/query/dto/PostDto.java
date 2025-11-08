package com.example.simple_cqrs.query.dto;

import com.example.simple_cqrs.command.domain.PostStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private UUID postId;
    private String username;
    private String content;
    private PostStatus status;
    private Instant createdAt;
    private Instant approvedAt;
    private Instant rejectedAt;
    private String rejectionReason;
    private Integer likeCount;
    private Integer commentCount;
}
