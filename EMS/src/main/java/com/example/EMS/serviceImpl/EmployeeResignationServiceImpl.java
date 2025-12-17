package com.example.EMS.serviceImpl;

import com.example.EMS.dto.ResignationDetailsDTO;
import com.example.EMS.entity.ConsolidatedChecklist;
import com.example.EMS.entity.Employee;
import com.example.EMS.entity.EmployeeResignationDetails;
import com.example.EMS.entity.ItChecklist;
import com.example.EMS.entity.LoanChecklist;
import com.example.EMS.enums.ResignationStatus;
import com.example.EMS.repo.EmployeeDirectoryRepo;
import com.example.EMS.repo.EmployeeResignationDirectoryRepo;
import com.example.EMS.repo.ConsolidatedChecklistRepo;
import com.example.EMS.repo.ItChecklistRepo;
import com.example.EMS.repo.LoanChecklistRepo;
import com.example.EMS.service.EmployeeResignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmployeeResignationServiceImpl implements EmployeeResignationService {
    private final EmployeeResignationDirectoryRepo  employeeResignationDirectoryRepo;
    private final EmployeeDirectoryRepo employeeDirectoryRepo;
    private final ConsolidatedChecklistRepo consolidatedChecklistRepo;
    private final LoanChecklistRepo loanChecklistRepo;
    private final ItChecklistRepo itChecklistRepo;

    @Override
    public ResponseEntity<?> processEmployeeActions(ResignationDetailsDTO resignationDetails) {
        if(resignationDetails == null || resignationDetails.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        try{
            Employee employee = employeeDirectoryRepo.findByEmployeeNumber(resignationDetails.getEmployeeNumber());
            if (employee == null) {
                return ResponseEntity.notFound().build();
            }

            EmployeeResignationDetails employeeResignationDetails = employee.getResignationId() != null
                    ? employeeResignationDirectoryRepo.findByResignationId(employee.getResignationId())
                    : null;

            if (resignationDetails.getActionStatus() == ResignationStatus.SUBMITTTED) {
                if (employeeResignationDetails != null && !employeeResignationDetails.isRetained()) {
                    return ResponseEntity.badRequest().body("Employee already has an active resignation");
                }

                EmployeeResignationDetails newResignation = new EmployeeResignationDetails();
                newResignation.setResignationDate(new Date());
                newResignation.setResignationReason(resignationDetails.getReason());
                newResignation.setStatus(resignationDetails.getActionStatus());
                LocalDate lwdLocalDate = LocalDate.now().plusDays(30);

                Date lwdDate = Date.from(
                        lwdLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                );
                newResignation.setLwd(lwdDate);
                newResignation.setRetained(false);

                EmployeeResignationDetails savedRecord = employeeResignationDirectoryRepo.save(newResignation);
                employee.setResignationId(savedRecord.getResignationId());
                employeeDirectoryRepo.save(employee);

                ConsolidatedChecklist consolidatedChecklist = consolidatedChecklistRepo.findByResignationId(savedRecord.getResignationId());
                if (consolidatedChecklist == null) {
                    consolidatedChecklist = new ConsolidatedChecklist();
                    consolidatedChecklist.setResignationId(savedRecord.getResignationId());
                    consolidatedChecklist.setItChecklistClosed(false);
                    consolidatedChecklist.setLoanChecklistClosed(false);
                }
                consolidatedChecklist = consolidatedChecklistRepo.save(consolidatedChecklist);

                ensureChecklistsInitialized(consolidatedChecklist);
                return ResponseEntity.ok().body("Resignation details Submitted Successfully");
            }

            if (resignationDetails.getActionStatus() == ResignationStatus.VOLUNTARY_WITHDRAWAL) {
                if (employeeResignationDetails == null) {
                    return ResponseEntity.badRequest().body("No resignation found for employee");
                }
                employeeResignationDetails.setStatus(resignationDetails.getActionStatus());
                employeeResignationDetails.setRetained(true);
                employeeResignationDirectoryRepo.save(employeeResignationDetails);
                return ResponseEntity.ok().body("Resignation withdrawn successfully");
            }

            return ResponseEntity.badRequest().body("Invalid action for employee submission");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> processRmActions(ResignationDetailsDTO resignationDetails) {
        if(resignationDetails == null || resignationDetails.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Employee employee = employeeDirectoryRepo.findByEmployeeNumber(resignationDetails.getEmployeeNumber());
            if (employee == null) {
                return ResponseEntity.notFound().build();
            }
            EmployeeResignationDetails employeeResignationDetails = employeeResignationDirectoryRepo.findByResignationId(employee.getResignationId());
            if (employeeResignationDetails == null) {
                return ResponseEntity.notFound().build();
            }

            if (resignationDetails.getActionStatus() == ResignationStatus.APPROVED_BY_RM
                    || resignationDetails.getActionStatus() == ResignationStatus.SCHEDULED_MEETING) {
                employeeResignationDetails.setStatus(resignationDetails.getActionStatus());
                employeeResignationDirectoryRepo.save(employeeResignationDetails);
            } else {
                return ResponseEntity.badRequest().body("Invalid action for RM");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> processHrActions(ResignationDetailsDTO resignationDetails) {
        if(resignationDetails == null || resignationDetails.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Employee employee = employeeDirectoryRepo.findByEmployeeNumber(resignationDetails.getEmployeeNumber());
            if (employee == null) {
                return ResponseEntity.notFound().build();
            }
            EmployeeResignationDetails employeeResignationDetails = employeeResignationDirectoryRepo.findByResignationId(employee.getResignationId());
            if (employeeResignationDetails == null) {
                return ResponseEntity.notFound().build();
            }

            if (resignationDetails.getActionStatus() == ResignationStatus.APPROVED_BY_HR
                    || resignationDetails.getActionStatus() == ResignationStatus.REVERSE_TERMINATION) {
                if (resignationDetails.getActionStatus() == ResignationStatus.APPROVED_BY_HR) {
                    ConsolidatedChecklist consolidatedChecklist = consolidatedChecklistRepo.findByResignationId(employeeResignationDetails.getResignationId());
                    if (consolidatedChecklist == null || !consolidatedChecklist.isItChecklistClosed() || !consolidatedChecklist.isLoanChecklistClosed()) {
                        return ResponseEntity.badRequest().body("All checklists must be closed before HR approval");
                    }
                }
                employeeResignationDetails.setStatus(resignationDetails.getActionStatus());
                boolean retained = resignationDetails.getActionStatus() == ResignationStatus.REVERSE_TERMINATION;
                employeeResignationDetails.setRetained(retained);
                employeeResignationDirectoryRepo.save(employeeResignationDetails);
            } else {
                return ResponseEntity.badRequest().body("Invalid action for HR");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    private void ensureChecklistsInitialized(ConsolidatedChecklist consolidatedChecklist) {
        if (consolidatedChecklist == null || consolidatedChecklist.getChecklistId() == null) {
            return;
        }

        ItChecklist existingItChecklist = itChecklistRepo.findByChecklistId(consolidatedChecklist.getChecklistId());
        if (existingItChecklist == null) {
            ItChecklist itChecklist = new ItChecklist();
            itChecklist.setConsolidatedChecklist(consolidatedChecklist);
            itChecklist.setSubmitted(false);
            itChecklist.setDamaged(false);
            itChecklist.setPaid(false);
            itChecklistRepo.save(itChecklist);
        }

        LoanChecklist existingLoanChecklist = loanChecklistRepo.findByChecklistId(consolidatedChecklist.getChecklistId());
        if (existingLoanChecklist == null) {
            LoanChecklist loanChecklist = new LoanChecklist();
            loanChecklist.setConsolidatedChecklist(consolidatedChecklist);
            loanChecklist.setLoanTaken(false);
            loanChecklist.setRepaid(false);
            loanChecklistRepo.save(loanChecklist);
        }
    }

}
