package com.stackcraft.retroflow.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;

/**
 * A Team has a name and a list of members (member names only — no
 * authentication). A team must have at least one member.
 */
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "team_members", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "member_name")
    private List<String> members = new ArrayList<>();

    protected Team() {
        // JPA
    }

    public Team(String name, List<String> members) {
        this.name = name;
        this.members = members == null ? new ArrayList<>() : new ArrayList<>(members);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

}
