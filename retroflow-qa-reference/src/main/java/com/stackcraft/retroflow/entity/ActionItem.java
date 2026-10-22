package com.stackcraft.retroflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * An ActionItem belongs to a Retrospective: content, who submitted it, a
 * priority, and a completed flag. A completed action item can never be
 * uncompleted. Action items must be retrievable filtered by priority and
 * by completed status.
 */
@Entity
public class ActionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "retrospective_id", nullable = false)
    private Retrospective retrospective;

    private String content;

    @Enumerated(EnumType.STRING)
    private ActionItemPriority priority;

    private String submittedBy;

    private boolean completed;

    protected ActionItem() {
        // JPA
    }

    public ActionItem(Retrospective retrospective, String content, ActionItemPriority priority, String submittedBy) {
        this.retrospective = retrospective;
        this.content = content;
        this.priority = priority;
        this.submittedBy = submittedBy;
        this.completed = false;
    }

    public Long getId() {
        return id;
    }

    public Retrospective getRetrospective() {
        return retrospective;
    }

    public String getContent() {
        return content;
    }

    public ActionItemPriority getPriority() {
        return priority;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
