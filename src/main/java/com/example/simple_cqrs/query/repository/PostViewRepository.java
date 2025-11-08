package com.example.simple_cqrs.query.repository;

import com.example.simple_cqrs.command.domain.PostStatus;
import com.example.simple_cqrs.query.model.PostView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostViewRepository extends JpaRepository<PostView, UUID> {
    Page<PostView> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    Page<PostView> findByStatusOrderByApprovedAtDesc(PostStatus status, Pageable pageable);

}
