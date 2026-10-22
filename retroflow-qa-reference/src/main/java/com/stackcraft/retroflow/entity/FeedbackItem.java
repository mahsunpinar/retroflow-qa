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
 * A FeedbackItem belongs to a Retrospective: content, a type (WENT_WELL or
 * NEEDS_IMPROVEMENT — action items are their own entity, see ActionItem),
 * and who submitted it. Items can be added to an OPEN retrospective; once
 * the retrospective is CLOSED its feedback items can never be modified or
 * deleted.
 */
@Entity
public class FeedbackItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "retrospective_id", nullable = false)
    private Retrospective retrospective;

    private String content;

    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    private String submittedBy;

    protected FeedbackItem() {
        // JPA
    }

    public FeedbackItem(Retrospective retrospective, String content, FeedbackType type, String submittedBy) {
        this.retrospective = retrospective;
        this.content = content;
        this.type = type;
        this.submittedBy = submittedBy;
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

    public void setContent(String content) {
        this.content = content;
    }

    public FeedbackType getType() {
        return type;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

}
