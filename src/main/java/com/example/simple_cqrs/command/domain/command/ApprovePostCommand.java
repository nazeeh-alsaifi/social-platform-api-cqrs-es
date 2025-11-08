package com.example.simple_cqrs.command.domain.command;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ApprovePostCommand {
    @NotNull
    private UUID postId;

}
