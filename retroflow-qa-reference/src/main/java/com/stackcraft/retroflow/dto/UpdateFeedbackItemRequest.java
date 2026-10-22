package com.stackcraft.retroflow.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateFeedbackItemRequest(
        @NotBlank(message = "content is required") String content
) {
}
