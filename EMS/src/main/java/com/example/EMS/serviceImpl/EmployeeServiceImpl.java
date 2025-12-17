package com.example.EMS.serviceImpl;


import com.example.EMS.dto.ResignedEmployeesDTO;
import com.example.EMS.dto.ResponseDataBeanDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDirectoryRepo employeeDirectoryRepo;
    private final EmployeeResignationDirectoryRepo employeeResignationDirectoryRepo;

    @Override
    public ResponseEntity<?> createUser(Employee employee){
        log.info("Inside createUser - {}", employee);
        if (employee == null || employee.getEmployeeNumber() == null) {
            return ResponseEntity.badRequest().body("Employee details are required");
        }
        try{
            Employee savedEmployee = employeeDirectoryRepo.save(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
        }catch (Exception e){
            log.error("Error while creating user", e);
            return ResponseEntity.badRequest().build();
        }
    }


    @Override
    public ResponseEntity<?> login(Employee employee) {
        log.info("Inside login - {}", employee);
        try{
            Employee existingUser = employeeDirectoryRepo.findByEmployeeNumber(employee.getEmployeeNumber());
            if(existingUser == null){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            EmployeeResignationDetails employeeResignationDetails = null;
            if (existingUser.getResignationId() != null) {
                employeeResignationDetails = employeeResignationDirectoryRepo.findByResignationId(existingUser.getResignationId());
            }

            List<Employee> reporteeList = new ArrayList<>();
            List<Employee> rmReporteeList = employeeDirectoryRepo.findByRmEmployeeNumber(existingUser.getEmployeeNumber());
            if(!rmReporteeList.isEmpty()){
                reporteeList.addAll(rmReporteeList);
            }

            List<Employee> hrReporteeList = employeeDirectoryRepo.findByHrEmployeeNumber(existingUser.getEmployeeNumber());
            if(!hrReporteeList.isEmpty()){
                reporteeList.addAll(hrReporteeList);
            }

            Set<Integer> seenEmployeeNumbers = new HashSet<>();
            List<ResignedEmployeesDTO> resignedEmployeesList = new ArrayList<>();
            for(Employee reportee: reporteeList){
                if (reportee.getEmployeeNumber() != null && !seenEmployeeNumbers.add(reportee.getEmployeeNumber())) {
                    continue;
                }
                if(reportee.getResignationId() != null){
                    EmployeeResignationDetails reporteeResignationDetails =
                            employeeResignationDirectoryRepo.findByResignationId(reportee.getResignationId());
                    if (reporteeResignationDetails != null && !reporteeResignationDetails.isRetained()){
                        ResignedEmployeesDTO resignedEmployeesDTO = new ResignedEmployeesDTO(reportee, reporteeResignationDetails);
                        resignedEmployeesList.add(resignedEmployeesDTO);
                    }
                }
            }

            return ResponseEntity.ok(new ResponseDataBeanDTO(
                    employeeResignationDetails,
                    resignedEmployeesList
            ));

        }catch (Exception e){
            log.error("Error while processing login", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
