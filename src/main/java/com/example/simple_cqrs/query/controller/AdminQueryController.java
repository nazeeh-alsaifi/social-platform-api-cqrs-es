package com.example.simple_cqrs.query.controller;

import com.example.simple_cqrs.query.dto.PageResponse;
import com.example.simple_cqrs.query.dto.PostDto;
import com.example.simple_cqrs.query.service.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/queries/posts")
@RequiredArgsConstructor
public class AdminQueryController {

    private final PostQueryService postQueryService;

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<PostDto>> getPendingPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<PostDto> response = postQueryService.getPendingPosts(page, size);
        return ResponseEntity.ok(response);
    }
}
