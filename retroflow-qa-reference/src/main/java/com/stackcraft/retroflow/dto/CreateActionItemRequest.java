package com.stackcraft.retroflow.dto;

import com.stackcraft.retroflow.entity.ActionItemPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateActionItemRequest(
        @NotBlank(message = "content is required") String content,
        @NotNull(message = "priority is required") ActionItemPriority priority,
        @NotBlank(message = "submittedBy is required") String submittedBy
) {
}
