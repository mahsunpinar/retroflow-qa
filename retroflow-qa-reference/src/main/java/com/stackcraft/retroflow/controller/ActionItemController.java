package com.stackcraft.retroflow.controller;

import com.stackcraft.retroflow.dto.CompleteActionItemRequest;
import com.stackcraft.retroflow.dto.CreateActionItemRequest;
import com.stackcraft.retroflow.entity.ActionItem;
import com.stackcraft.retroflow.entity.ActionItemPriority;
import com.stackcraft.retroflow.service.ActionItemService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActionItemController {

    private final ActionItemService actionItemService;

    public ActionItemController(ActionItemService actionItemService) {
        this.actionItemService = actionItemService;
    }

    @PostMapping("/api/retrospectives/{retrospectiveId}/action-items")
    public ResponseEntity<ActionItem> addActionItem(
            @PathVariable Long retrospectiveId,
            @Valid @RequestBody CreateActionItemRequest request
    ) {
        ActionItem item = actionItemService.addActionItem(
                retrospectiveId, request.content(), request.priority(), request.submittedBy());
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PatchMapping("/api/action-items/{id}/complete")
    public ActionItem setCompleted(@PathVariable Long id, @Valid @RequestBody CompleteActionItemRequest request) {
        return actionItemService.setCompleted(id, request.completed());
    }

    @GetMapping("/api/action-items")
    public List<ActionItem> filterActionItems(
            @RequestParam(required = false) ActionItemPriority priority,
            @RequestParam(required = false) Boolean completed
    ) {
        return actionItemService.filter(priority, completed);
    }

}
