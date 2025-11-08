package com.example.simple_cqrs.command.controller;

import com.example.simple_cqrs.command.domain.command.CreatePostCommand;
import com.example.simple_cqrs.command.domain.PostStatus;
import com.example.simple_cqrs.command.domain.command.LikePostCommand;
import com.example.simple_cqrs.command.service.PostCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/commands/posts")
@RequiredArgsConstructor
public class PostCommandController {

    private final PostCommandService postCommandService;


    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody @Valid  CreatePostCommand command) {
        UUID postId = postCommandService.createPost(command);

        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId.toString());
        response.put("status", PostStatus.PENDING);
        response.put("createdAt", Instant.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> likePost(@RequestBody @Valid LikePostCommand command) {
        UUID postId = postCommandService.likePost(command);

        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId.toString());
        response.put("createdAt", Instant.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
