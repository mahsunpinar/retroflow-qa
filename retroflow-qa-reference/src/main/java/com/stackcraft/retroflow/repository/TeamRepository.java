package com.stackcraft.retroflow.repository;

import com.stackcraft.retroflow.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
