package com.stackcraft.retroflow.repository;

import com.stackcraft.retroflow.entity.ActionItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {

    List<ActionItem> findByRetrospectiveId(Long retrospectiveId);

}
