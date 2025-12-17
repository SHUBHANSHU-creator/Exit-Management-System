package com.example.EMS.serviceImpl;

import com.example.EMS.dto.ChecklistCloseRequest;
import com.example.EMS.dto.ChecklistViewRequest;
import com.example.EMS.entity.ConsolidatedChecklist;
import com.example.EMS.entity.Employee;
import com.example.EMS.entity.ItChecklist;
import com.example.EMS.entity.LoanChecklist;
import com.example.EMS.enums.ChecklistType;
import com.example.EMS.repo.ConsolidatedChecklistRepo;
import com.example.EMS.repo.EmployeeDirectoryRepo;
import com.example.EMS.repo.ItChecklistRepo;
import com.example.EMS.repo.LoanChecklistRepo;
import com.example.EMS.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChecklistServiceImpl implements ChecklistService {

    private final EmployeeDirectoryRepo employeeDirectoryRepo;
    private final ConsolidatedChecklistRepo consolidatedChecklistRepo;
    private final LoanChecklistRepo loanChecklistRepo;
    private final ItChecklistRepo itChecklistRepo;

    @Override
    public ResponseEntity<?> viewPendingChecklists(ChecklistViewRequest request) {
        log.info("Inside viewPendingChecklists - {}", request);
        if (request == null || request.getChecklistType() == null) {
            return ResponseEntity.badRequest().body("Checklist type is required");
        }

        List<ConsolidatedChecklist> consolidatedChecklists;
        if (request.getChecklistType() == ChecklistType.IT) {
            consolidatedChecklists = consolidatedChecklistRepo.findByItChecklistClosedFalse();
        } else if (request.getChecklistType() == ChecklistType.LOAN) {
            consolidatedChecklists = consolidatedChecklistRepo.findByLoanChecklistClosedFalse();
        } else {
            return ResponseEntity.badRequest().body("Unsupported checklist type");
        }

        List<Employee> pendingEmployees = new ArrayList<>();
        for (ConsolidatedChecklist consolidatedChecklist : consolidatedChecklists) {
            if (consolidatedChecklist.getResignationId() == null) {
                continue;
            }
            Employee employee = employeeDirectoryRepo.findByResignationId(consolidatedChecklist.getResignationId());
            if (employee != null) {
                pendingEmployees.add(employee);
            }
        }

        return ResponseEntity.ok(pendingEmployees);
    }

    @Override
    public ResponseEntity<?> closeChecklist(ChecklistCloseRequest request) {
        log.info("Inside closeChecklist - {}", request);
        if (request == null || request.getChecklistType() == null || request.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().body("Checklist type and employee number are required");
        }

        Employee employee = employeeDirectoryRepo.findByEmployeeNumber(request.getEmployeeNumber());
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        if (employee.getResignationId() == null) {
            return ResponseEntity.badRequest().body("Employee has no active resignation");
        }

        ConsolidatedChecklist consolidatedChecklist = consolidatedChecklistRepo.findByResignationId(employee.getResignationId());
        if (consolidatedChecklist == null) {
            consolidatedChecklist = new ConsolidatedChecklist();
            consolidatedChecklist.setResignationId(employee.getResignationId());
            consolidatedChecklist.setItChecklistClosed(false);
            consolidatedChecklist.setLoanChecklistClosed(false);
            consolidatedChecklist = consolidatedChecklistRepo.save(consolidatedChecklist);
        }

        if (request.getChecklistType() == ChecklistType.IT) {
            if (request.getSubmitted() == null || request.getDamaged() == null || request.getPaid() == null) {
                return ResponseEntity.badRequest().body("Submitted, damaged and paid fields are required for IT checklist");
            }

            ItChecklist itChecklist = itChecklistRepo.findByChecklistId(consolidatedChecklist.getChecklistId());
            if (itChecklist == null) {
                itChecklist = new ItChecklist();
                itChecklist.setConsolidatedChecklist(consolidatedChecklist);
            }
            itChecklist.setSubmitted(request.getSubmitted());
            itChecklist.setDamaged(request.getDamaged());
            itChecklist.setPaid(request.getPaid());
            itChecklistRepo.save(itChecklist);

            consolidatedChecklist.setItChecklistClosed(true);
            consolidatedChecklistRepo.save(consolidatedChecklist);
        } else if (request.getChecklistType() == ChecklistType.LOAN) {
            if (request.getLoanTaken() == null || request.getRepaid() == null) {
                return ResponseEntity.badRequest().body("loanTaken and repaid fields are required for Loan checklist");
            }

            LoanChecklist loanChecklist = loanChecklistRepo.findByChecklistId(consolidatedChecklist.getChecklistId());
            if (loanChecklist == null) {
                loanChecklist = new LoanChecklist();
                loanChecklist.setConsolidatedChecklist(consolidatedChecklist);
            }
            loanChecklist.setLoanTaken(request.getLoanTaken());
            loanChecklist.setRepaid(request.getRepaid());
            loanChecklistRepo.save(loanChecklist);

            consolidatedChecklist.setLoanChecklistClosed(true);
            consolidatedChecklistRepo.save(consolidatedChecklist);
        } else {
            return ResponseEntity.badRequest().body("Unsupported checklist type");
        }

        return ResponseEntity.ok().body("Checklist closed successfully");
    }
}
