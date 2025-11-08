package com.example.simple_cqrs.query.service;

import com.example.simple_cqrs.command.domain.PostStatus;
import com.example.simple_cqrs.query.dto.PageResponse;
import com.example.simple_cqrs.query.dto.PostDto;
import com.example.simple_cqrs.query.model.PostView;
import com.example.simple_cqrs.query.repository.PostViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostViewRepository postViewRepository;

    public PageResponse<PostDto> getUserPosts(String userId, int page, int size) {
        if (size > 20) size = 20;

        Pageable pageable = PageRequest.of(page, size);
        Page<PostView> postPage = postViewRepository.findByUserIdOrderByCreatedAtDesc(
                userId, pageable);

        return mapToPageResponse(postPage);
    }

    public PageResponse<PostDto> getApprovedFeed(int page, int size) {
        if (size > 20) size = 20;

        Pageable pageable = PageRequest.of(page, size);

        Page<PostView> postFeed = postViewRepository.findByStatusOrderByApprovedAtDesc(PostStatus.APPROVED, pageable);

        return mapToPageResponse(postFeed);
    }

    private PageResponse<PostDto> mapToPageResponse(Page<PostView> postPage) {
        return new PageResponse<>(
                postPage.getContent().stream().map(this::mapToDto).toList(),
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages()
        );
    }

    private PostDto mapToDto(PostView postView) {
        PostDto dto = new PostDto();
        dto.setPostId(postView.getPostId());
        dto.setUsername(postView.getUsername());
        dto.setContent(postView.getContent());
        dto.setStatus(postView.getStatus());
        dto.setCreatedAt(postView.getCreatedAt());
        dto.setApprovedAt(postView.getApprovedAt());
        dto.setRejectionReason(postView.getRejectionReason());
        dto.setLikeCount(postView.getLikeCount());
        dto.setCommentCount(postView.getCommentCount());
        return dto;
    }


}
