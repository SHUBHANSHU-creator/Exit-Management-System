package com.example.EMS.controller;

import com.example.EMS.dto.ChecklistCloseRequest;
import com.example.EMS.dto.ChecklistViewRequest;
import com.example.EMS.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checklists")
@RequiredArgsConstructor
@Slf4j
public class ChecklistController {

    private final ChecklistService checklistService;

    @PostMapping("/pending")
    public ResponseEntity<?> viewPendingChecklists(@RequestBody ChecklistViewRequest request) {
        log.info("View pending checklists - {}", request);
        return checklistService.viewPendingChecklists(request);
    }

    @PostMapping("/close")
    public ResponseEntity<?> closeChecklist(@RequestBody ChecklistCloseRequest request) {
        log.info("Close checklist - {}", request);
        return checklistService.closeChecklist(request);
    }
}
