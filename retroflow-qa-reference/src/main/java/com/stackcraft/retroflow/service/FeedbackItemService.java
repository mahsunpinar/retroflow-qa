package com.stackcraft.retroflow.service;

import com.stackcraft.retroflow.entity.FeedbackItem;
import com.stackcraft.retroflow.entity.FeedbackType;
import com.stackcraft.retroflow.entity.Retrospective;
import com.stackcraft.retroflow.entity.RetrospectiveStatus;
import com.stackcraft.retroflow.exception.ResourceNotFoundException;
import com.stackcraft.retroflow.exception.RetroflowException;
import com.stackcraft.retroflow.repository.FeedbackItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackItemService {

    private final FeedbackItemRepository feedbackItemRepository;
    private final RetrospectiveService retrospectiveService;

    public FeedbackItemService(FeedbackItemRepository feedbackItemRepository, RetrospectiveService retrospectiveService) {
        this.feedbackItemRepository = feedbackItemRepository;
        this.retrospectiveService = retrospectiveService;
    }

    @Transactional
    public FeedbackItem addFeedbackItem(Long retrospectiveId, String content, FeedbackType type, String submittedBy) {
        Retrospective retrospective = retrospectiveService.getRetrospectiveOrThrow(retrospectiveId);
        if (retrospective.getStatus() == RetrospectiveStatus.CLOSED) {
            throw new RetroflowException(
                    "Retrospective " + retrospectiveId + ": cannot add feedback to a closed retrospective");
        }
        FeedbackItem item = new FeedbackItem(retrospective, content, type, submittedBy);
        return feedbackItemRepository.save(item);
    }

    public List<FeedbackItem> getFeedbackItemsForRetrospective(Long retrospectiveId) {
        return feedbackItemRepository.findByRetrospectiveId(retrospectiveId);
    }

    @Transactional
    public FeedbackItem updateFeedbackItem(Long id, String content) {
        FeedbackItem item = feedbackItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedbackItem " + id + ": not found"));
        if (item.getRetrospective().getStatus() == RetrospectiveStatus.CLOSED) {
            throw new RetroflowException(
                    "FeedbackItem " + id + ": cannot modify feedback in a closed retrospective");
        }
        item.setContent(content);
        return feedbackItemRepository.save(item);
    }

    @Transactional
    public void deleteFeedbackItem(Long id) {
        FeedbackItem item = feedbackItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedbackItem " + id + ": not found"));
        feedbackItemRepository.delete(item);
    }

}
