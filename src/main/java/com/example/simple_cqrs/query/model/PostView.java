package com.example.simple_cqrs.query.model;

import com.example.simple_cqrs.command.domain.PostStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "posts_view_v1")
@NoArgsConstructor
public class PostView {

    @Id
    private UUID postId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant approvedAt;

    private Instant rejectedAt;

    private String rejectionReason;

    @Version
    private Long version;

    public PostView(UUID postId, String userId, String username, String content, PostStatus postStatus,
                    Instant eventTimestamp) {
        this.postId=postId;
        this.userId=userId;
        this.username=username;
        this.content=content;
        this.status=postStatus;
        this.createdAt=eventTimestamp;

    }
}
