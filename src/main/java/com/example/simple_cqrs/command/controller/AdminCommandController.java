package com.example.simple_cqrs.command.controller;

import com.example.simple_cqrs.command.domain.command.ApprovePostCommand;
import com.example.simple_cqrs.command.domain.command.RejectPostCommand;
import com.example.simple_cqrs.command.service.AdminCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/commands/posts")
@RequiredArgsConstructor
public class AdminCommandController {

    private final AdminCommandService adminCommandService;

    @PostMapping("/approve")
    public ResponseEntity<Void> approvePost(@RequestBody @Valid ApprovePostCommand approvePostCommand) {
        adminCommandService.approvePost(approvePostCommand);



        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectPost(@RequestBody @Valid RejectPostCommand rejectPostCommand) {
        adminCommandService.rejectPost(rejectPostCommand);


        return ResponseEntity.ok().build();
    }
}
