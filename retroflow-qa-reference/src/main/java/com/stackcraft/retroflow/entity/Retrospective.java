package com.stackcraft.retroflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * A Retrospective belongs to a Team, has a title, a date, and a status.
 * A team can have multiple retrospectives but only one OPEN at a time.
 * Once closed, a retrospective can never be reopened (there is
 * deliberately no "reopen" operation anywhere in this codebase).
 */
@Entity
public class Retrospective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    private String title;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private RetrospectiveStatus status;

    protected Retrospective() {
        // JPA
    }

    public Retrospective(Team team, String title) {
        this.team = team;
        this.title = title;
        this.date = LocalDate.now();
        this.status = RetrospectiveStatus.OPEN;
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public RetrospectiveStatus getStatus() {
        return status;
    }

    public void setStatus(RetrospectiveStatus status) {
        this.status = status;
    }

}
