package com.stackcraft.retroflow.controller;

import com.stackcraft.retroflow.dto.CreateTeamRequest;
import com.stackcraft.retroflow.entity.Team;
import com.stackcraft.retroflow.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<Team> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        Team team = teamService.createTeam(request.name(), request.members());
        return ResponseEntity.status(HttpStatus.CREATED).body(team);
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id) {
        return teamService.getTeam(id);
    }

}
