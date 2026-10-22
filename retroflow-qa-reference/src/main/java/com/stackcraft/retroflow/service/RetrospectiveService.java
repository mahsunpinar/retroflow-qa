package com.stackcraft.retroflow.service;

import com.stackcraft.retroflow.entity.Retrospective;
import com.stackcraft.retroflow.entity.RetrospectiveStatus;
import com.stackcraft.retroflow.entity.Team;
import com.stackcraft.retroflow.exception.ResourceNotFoundException;
import com.stackcraft.retroflow.exception.RetroflowException;
import com.stackcraft.retroflow.repository.RetrospectiveRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetrospectiveService {

    private final RetrospectiveRepository retrospectiveRepository;
    private final TeamService teamService;

    public RetrospectiveService(RetrospectiveRepository retrospectiveRepository, TeamService teamService) {
        this.retrospectiveRepository = retrospectiveRepository;
        this.teamService = teamService;
    }

    @Transactional
    public Retrospective createRetrospective(Long teamId, String title) {
        Team team = teamService.getTeam(teamId);
        boolean hasOpen = retrospectiveRepository.existsByTeamIdAndStatus(teamId, RetrospectiveStatus.OPEN);
        if (hasOpen) {
            throw new RetroflowException("Team " + teamId + ": already has an open retrospective");
        }
        Retrospective retrospective = new Retrospective(team, title);
        return retrospectiveRepository.save(retrospective);
    }

    public List<Retrospective> getRetrospectivesForTeam(Long teamId) {
        return retrospectiveRepository.findByTeamId(teamId);
    }

    /**
     * Used internally by other services that need to look up a retrospective
     * as part of a larger operation (e.g. adding a feedback item, closing).
     */
    Retrospective getRetrospectiveOrThrow(Long id) {
        return retrospectiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Retrospective " + id + ": not found"));
    }

    /**
     * Used directly by the "get retrospective by id" read endpoint.
     */
    public Retrospective getRetrospective(Long id) {
        return retrospectiveRepository.getReferenceById(id);
    }

    @Transactional
    public Retrospective closeRetrospective(Long id) {
        Retrospective retrospective = getRetrospectiveOrThrow(id);
        retrospective.setStatus(RetrospectiveStatus.CLOSED);
        return retrospectiveRepository.save(retrospective);
    }

}
