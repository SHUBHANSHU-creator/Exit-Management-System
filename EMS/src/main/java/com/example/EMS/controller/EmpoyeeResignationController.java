package com.example.EMS.controller;

import com.example.EMS.dto.ResignationDetailsDTO;
import com.example.EMS.service.EmployeeResignationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmpoyeeResignationController {
    private final EmployeeResignationService employeeResignationService;

    @PostMapping("/employeeSubmission")
    public ResponseEntity<?> employeeRequest(ResignationDetailsDTO resignationDetails) {
        log.info("Inside employeeRequest - {}", resignationDetails);
        if (resignationDetails.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        employeeResignationService.processEmployeeActions(resignationDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rmApproval")
    public ResponseEntity<?> rmRequest(ResignationDetailsDTO resignationDetails) {
        log.info("Inside employeeRequest - {}", resignationDetails);
        if (resignationDetails.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        employeeResignationService.processRmActions(resignationDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hrApproval")
    public ResponseEntity<?> hrRequest(ResignationDetailsDTO resignationDetails) {
        log.info("Inside employeeRequest - {}", resignationDetails);
        if (resignationDetails.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        employeeResignationService.processHrActions(resignationDetails);
        return ResponseEntity.ok().build();
    }
}
