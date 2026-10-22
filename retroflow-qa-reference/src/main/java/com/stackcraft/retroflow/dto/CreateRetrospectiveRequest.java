package com.stackcraft.retroflow.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRetrospectiveRequest(
        @NotBlank(message = "title is required") String title
) {
}
