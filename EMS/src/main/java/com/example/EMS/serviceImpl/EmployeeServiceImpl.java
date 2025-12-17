package com.example.EMS.serviceImpl;


import com.example.EMS.dto.ResignationDetailsDTO;
import com.example.EMS.dto.ResignedEmployeesDTO;
import com.example.EMS.entity.Employee;
import com.example.EMS.entity.EmployeeResignationDetails;
import com.example.EMS.repo.EmployeeDirectoryRepo;
import com.example.EMS.repo.EmployeeResignationDirectoryRepo;
import com.example.EMS.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDirectoryRepo employeeDirectoryRepo;
    private final EmployeeResignationDirectoryRepo employeeResignationDirectoryRepo;

    @Override
    public ResponseEntity<?> createUser(Employee employee){
        log.info("Inside createUser - {}", employee);
        try{
            employeeDirectoryRepo.save(employee);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<?> login(Employee employee) {
        log.info("Inside login - {}", employee);
        try{
            Employee existingUser = employeeDirectoryRepo.existsByEmployeeNumber(employee.getEmployeeNumber());
            if(existingUser != null){
                EmployeeResignationDetails employeeResignationDetails = employeeResignationDirectoryRepo.findByResignationId(existingUser.getResignationId());
                List<Employee> reporteeList = employeeDirectoryRepo.findByRmEmployeeNumber(existingUser.getEmployeeNumber());
                List<ResignedEmployeesDTO> resignedEmployeesList = new ArrayList<>();
                for(Employee reportee: reporteeList){
                    if(reportee.getResignationId() != null){
                        EmployeeResignationDetails reporteeResignationDetails =  employeeResignationDirectoryRepo.findByResignationId(reportee.getResignationId());
                        if (reporteeResignationDetails != null){
                            ResignedEmployeesDTO resignedEmployeesDTO = new ResignedEmployeesDTO(reportee, reporteeResignationDetails);
                            resignedEmployeesList.add(resignedEmployeesDTO);
                        }
                    }
                }

                return ResponseEntity.ok(resignedEmployeesList);

            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return null;
    }
}
