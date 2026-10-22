package com.stackcraft.retroflow.controller;

import com.stackcraft.retroflow.dto.CreateRetrospectiveRequest;
import com.stackcraft.retroflow.entity.Retrospective;
import com.stackcraft.retroflow.service.RetrospectiveService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetrospectiveController {

    private final RetrospectiveService retrospectiveService;

    public RetrospectiveController(RetrospectiveService retrospectiveService) {
        this.retrospectiveService = retrospectiveService;
    }

    @PostMapping("/api/teams/{teamId}/retrospectives")
    public ResponseEntity<Retrospective> createRetrospective(
            @PathVariable Long teamId,
            @Valid @RequestBody CreateRetrospectiveRequest request
    ) {
        Retrospective retrospective = retrospectiveService.createRetrospective(teamId, request.title());
        return ResponseEntity.ok(retrospective);
    }

    @GetMapping("/api/teams/{teamId}/retrospectives")
    public List<Retrospective> getRetrospectivesForTeam(@PathVariable Long teamId) {
        return retrospectiveService.getRetrospectivesForTeam(teamId);
    }

    @GetMapping("/api/retrospectives/{id}")
    public Retrospective getRetrospective(@PathVariable Long id) {
        return retrospectiveService.getRetrospective(id);
    }

    @PutMapping("/api/retrospectives/{id}/close")
    public Retrospective closeRetrospective(@PathVariable Long id) {
        return retrospectiveService.closeRetrospective(id);
    }

}
