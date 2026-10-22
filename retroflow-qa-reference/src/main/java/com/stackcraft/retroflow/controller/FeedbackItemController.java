package com.stackcraft.retroflow.controller;

import com.stackcraft.retroflow.dto.CreateFeedbackItemRequest;
import com.stackcraft.retroflow.dto.UpdateFeedbackItemRequest;
import com.stackcraft.retroflow.entity.FeedbackItem;
import com.stackcraft.retroflow.service.FeedbackItemService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackItemController {

    private final FeedbackItemService feedbackItemService;

    public FeedbackItemController(FeedbackItemService feedbackItemService) {
        this.feedbackItemService = feedbackItemService;
    }

    @PostMapping("/api/retrospectives/{retrospectiveId}/feedback-items")
    public ResponseEntity<FeedbackItem> addFeedbackItem(
            @PathVariable Long retrospectiveId,
            @Valid @RequestBody CreateFeedbackItemRequest request
    ) {
        FeedbackItem item = feedbackItemService.addFeedbackItem(
                retrospectiveId, request.content(), request.type(), request.submittedBy());
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping("/api/retrospectives/{retrospectiveId}/feedback-items")
    public List<FeedbackItem> getFeedbackItems(@PathVariable Long retrospectiveId) {
        return feedbackItemService.getFeedbackItemsForRetrospective(retrospectiveId);
    }

    @PutMapping("/api/feedback-items/{id}")
    public FeedbackItem updateFeedbackItem(@PathVariable Long id, @Valid @RequestBody UpdateFeedbackItemRequest request) {
        return feedbackItemService.updateFeedbackItem(id, request.content());
    }

    @DeleteMapping("/api/feedback-items/{id}")
    public ResponseEntity<Void> deleteFeedbackItem(@PathVariable Long id) {
        feedbackItemService.deleteFeedbackItem(id);
        return ResponseEntity.noContent().build();
    }

}
