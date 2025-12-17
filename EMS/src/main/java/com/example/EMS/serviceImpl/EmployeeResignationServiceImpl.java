package com.example.EMS.serviceImpl;

import com.example.EMS.dto.ResignationDetailsDTO;
import com.example.EMS.entity.Employee;
import com.example.EMS.entity.EmployeeResignationDetails;
import com.example.EMS.repo.EmployeeDirectoryRepo;
import com.example.EMS.repo.EmployeeResignationDirectoryRepo;
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

    @Override
    public ResponseEntity<?> processEmployeeActions(ResignationDetailsDTO resignationDetails) {
        if(resignationDetails == null && resignationDetails.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        try{
            EmployeeResignationDetails employeeResignationDetails = new EmployeeResignationDetails();
            switch (resignationDetails.getActionStatus()){
                case SUBMITTTED:
                    employeeResignationDetails.setResignationDate(new Date());
                    employeeResignationDetails.setResignationReason(resignationDetails.getReason());
                    employeeResignationDetails.setStatus(resignationDetails.getActionStatus());
                    LocalDate lwdLocalDate = LocalDate.now().plusDays(30);

                    Date lwdDate = Date.from(
                            lwdLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                    );
                    employeeResignationDetails.setLwd(lwdDate);
                case VOLUNTARY_WITHDRAWAL:
                    employeeResignationDetails.setStatus(resignationDetails.getActionStatus());
            }

            employeeResignationDirectoryRepo.save(employeeResignationDetails);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("Resignation details Submitted Successfully");

    }

    @Override
    public ResponseEntity<?> processRmActions(ResignationDetailsDTO resignationDetails) {
        if(resignationDetails == null && resignationDetails.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Employee employee = employeeDirectoryRepo.findByEmployeeNumber(resignationDetails.getEmployeeNumber());
            EmployeeResignationDetails employeeResignationDetails = employeeResignationDirectoryRepo.findByResignationId(employee.getResignationId());
            employeeResignationDetails.setStatus(resignationDetails.getActionStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> processHrActions(ResignationDetailsDTO resignationDetails) {
        if(resignationDetails == null && resignationDetails.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Employee employee = employeeDirectoryRepo.findByEmployeeNumber(resignationDetails.getEmployeeNumber());
            EmployeeResignationDetails employeeResignationDetails = employeeResignationDirectoryRepo.findByResignationId(employee.getResignationId());
            employeeResignationDetails.setStatus(resignationDetails.getActionStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }
}
