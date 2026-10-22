package com.stackcraft.retroflow.repository;

import com.stackcraft.retroflow.entity.Retrospective;
import com.stackcraft.retroflow.entity.RetrospectiveStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetrospectiveRepository extends JpaRepository<Retrospective, Long> {

    List<Retrospective> findByTeamId(Long teamId);

    boolean existsByTeamIdAndStatus(Long teamId, RetrospectiveStatus status);

}
