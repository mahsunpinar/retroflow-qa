package com.stackcraft.retroflow.dto;

import com.stackcraft.retroflow.entity.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFeedbackItemRequest(
        @NotBlank(message = "content is required") String content,
        @NotNull(message = "type is required") FeedbackType type,
        @NotBlank(message = "submittedBy is required") String submittedBy
) {
}
