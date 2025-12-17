package com.example.EMS.service;

import com.example.EMS.dto.ChecklistCloseRequest;
import com.example.EMS.dto.ChecklistViewRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ChecklistService {
    ResponseEntity<?> viewPendingChecklists(ChecklistViewRequest request);

    ResponseEntity<?> closeChecklist(ChecklistCloseRequest request);
}
