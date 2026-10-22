package com.stackcraft.retroflow.repository;

import com.stackcraft.retroflow.entity.FeedbackItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackItemRepository extends JpaRepository<FeedbackItem, Long> {

    List<FeedbackItem> findByRetrospectiveId(Long retrospectiveId);

}
