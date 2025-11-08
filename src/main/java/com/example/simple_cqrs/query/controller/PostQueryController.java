package com.example.simple_cqrs.query.controller;

import com.example.simple_cqrs.query.dto.PageResponse;
import com.example.simple_cqrs.query.dto.PostDto;
import com.example.simple_cqrs.query.service.PostQueryService;
import com.example.simple_cqrs.shared.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/queries")
@RequiredArgsConstructor
public class PostQueryController {
    private final PostQueryService postQueryService;

    @GetMapping("/my-posts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponse<PostDto>> getMyPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String userId = SecurityUtils.getCurrentUsername();
        PageResponse<PostDto> userPosts = postQueryService.getUserPosts(userId, page, size);
        return ResponseEntity.ok(userPosts);
    }

    @GetMapping("/feed")
    public ResponseEntity<PageResponse<PostDto>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<PostDto> response = postQueryService.getApprovedFeed(page, size);
        return ResponseEntity.ok(response);
    }

}
