package com.stackcraft.retroflow.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateTeamRequest(
        @NotBlank(message = "name is required") String name,
        List<String> members
) {
}
