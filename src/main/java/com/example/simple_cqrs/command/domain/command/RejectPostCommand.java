package com.example.simple_cqrs.command.domain.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RejectPostCommand {

    @NotNull
    private UUID postId;

    @NotBlank
    private String reason;

}
