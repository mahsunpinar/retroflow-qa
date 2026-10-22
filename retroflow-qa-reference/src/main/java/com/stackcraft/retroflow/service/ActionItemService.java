package com.stackcraft.retroflow.service;

import com.stackcraft.retroflow.entity.ActionItem;
import com.stackcraft.retroflow.entity.ActionItemPriority;
import com.stackcraft.retroflow.entity.Retrospective;
import com.stackcraft.retroflow.exception.ResourceNotFoundException;
import com.stackcraft.retroflow.repository.ActionItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActionItemService {

    private final ActionItemRepository actionItemRepository;
    private final RetrospectiveService retrospectiveService;

    public ActionItemService(ActionItemRepository actionItemRepository, RetrospectiveService retrospectiveService) {
        this.actionItemRepository = actionItemRepository;
        this.retrospectiveService = retrospectiveService;
    }

    @Transactional
    public ActionItem addActionItem(Long retrospectiveId, String content, ActionItemPriority priority, String submittedBy) {
        Retrospective retrospective = retrospectiveService.getRetrospectiveOrThrow(retrospectiveId);
        ActionItem item = new ActionItem(retrospective, content, priority, submittedBy);
        return actionItemRepository.save(item);
    }

    @Transactional
    public ActionItem setCompleted(Long id, boolean completed) {
        ActionItem item = actionItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActionItem " + id + ": not found"));
        item.setCompleted(completed);
        return actionItemRepository.save(item);
    }

    public List<ActionItem> filter(ActionItemPriority priority, Boolean completed) {
        if (priority != null && completed != null) {
            return actionItemRepository.findAll().stream()
                    .filter(item -> item.getPriority() == priority || item.isCompleted() == completed)
                    .toList();
        }
        if (priority != null) {
            return actionItemRepository.findAll().stream()
                    .filter(item -> item.getPriority() == priority)
                    .toList();
        }
        if (completed != null) {
            return actionItemRepository.findAll().stream()
                    .filter(item -> item.isCompleted() == completed)
                    .toList();
        }
        return actionItemRepository.findAll();
    }

}
