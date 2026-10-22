package com.stackcraft.retroflow.dto;

import jakarta.validation.constraints.NotNull;

public record CompleteActionItemRequest(
        @NotNull(message = "completed is required") Boolean completed
) {
}
